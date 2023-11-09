package com.vsquad.iroas.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsquad.iroas.aggregate.dto.*;
import com.vsquad.iroas.aggregate.dto.request.ReqAvatarDto;
import com.vsquad.iroas.aggregate.dto.request.ReqPlayerDto;
import com.vsquad.iroas.aggregate.dto.response.ResMessageDto;
import com.vsquad.iroas.aggregate.dto.response.ResPlayerInfoDto;
import com.vsquad.iroas.aggregate.dto.response.ResTokenDto;
import com.vsquad.iroas.aggregate.dto.response.ResponseDto;
import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.config.exception.PlayerNotFoundException;
import com.vsquad.iroas.config.exception.SteamUserNotFoundException;
import com.vsquad.iroas.service.PlayerService;
import com.vsquad.iroas.service.auth.CustomTokenProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;


@RestController
@RequestMapping("/api/v1/player")
@Slf4j
@Tag(name = "플레이어 API")
public class PlayerController {

    private PlayerService playerService;

    private CustomTokenProviderService customTokenProviderService;

    @Value("${steam.api.key}")
    private String steamApiKey;

    private final WebClient webClient;

    public PlayerController(PlayerService playerService, CustomTokenProviderService customTokenProviderService, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://api.steampowered.com").build();
        this.playerService = playerService;
        this.customTokenProviderService = customTokenProviderService;
    }

    @GetMapping("/login")
    @Operation(summary = "로그인", description = "스팀에서 받아온 식별 정보를 가지고 로그인 합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "로그인 실패", content = @Content(schema = @Schema(name = "로그인 실패", example = "에러 메시지(스팀 유저를 찾을 수 없음 등)"), mediaType = "application/json"))
    })
    @Parameter(name = "steamKey", description = "스팀에서 받아온 회원 식별 정보", in = ParameterIn.QUERY)
    public ResponseEntity<ResTokenDto> steamLogin(@RequestParam String key, @RequestParam String type) {

        try {
            PlayerDto player = playerService.readPlayer(key, type);

            String token = customTokenProviderService.generateToken(player);

            ResTokenDto responseDto = new ResTokenDto(token, "로그인 성공");

            log.info("로그인 성공");
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (PlayerNotFoundException e) {
            log.warn(e.getMessage());

            ResTokenDto responseDto = new ResTokenDto(null, e.getMessage());
            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        } catch (SteamUserNotFoundException e) {
            log.warn(e.getMessage());
            log.warn("스팀 유저를 찾을 수 없음");

            ResTokenDto responseDto = new ResTokenDto(null, "스팀 유저를 찾을 수 없음");
            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.warn(e.getMessage());
            log.warn("로그인 실패");

            ResTokenDto responseDto = new ResTokenDto(null, "로그인 실패");
            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    @Operation(summary = "플레이어 추가", description = "스팀에서 받아온 식별 정보와 닉네임을 가지고 플레이어를 생성 합니다.", responses = {
            @ApiResponse(responseCode = "201", description = "플레이어 추가 성공", content = @Content(schema = @Schema(implementation = ResMessageDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "플레이어 추가 실패", content = @Content(schema = @Schema(name = "플레이어 추가 실패", example = "에러 메시지(닉네임 중복 등)"), mediaType = "application/json"))
    })
    public ResponseEntity<ResTokenDto> addPlayer(@RequestBody ReqPlayerDto reqBody) throws ParseException {

        String key = reqBody.getKey();
        String type = reqBody.getType();
        AtomicReference<String> nickname = new AtomicReference<>();

        if(key == null || key.isEmpty()) {
            throw new IllegalArgumentException("회원 식별 정보가 없습니다.");
        }

        try {
            PlayerDto player;

            if(type.equals("steam")) {

                Mono<String> json = webClient.get()
                        .uri(uriBuilder -> uriBuilder.path("/ISteamUser/GetPlayerSummaries/v0002/")
                                .queryParam("key", steamApiKey)
                                .queryParam("steamids", key)
                                .build())
                        .retrieve()
                        .bodyToMono(String.class)
                        .flatMap(response -> {

                            log.info("스팀 응답 값 {}", response);

                            JSONParser parser = new JSONParser();

                            try {
                                JSONObject obj = (JSONObject) parser.parse(response);
                                JSONObject responseObject = (JSONObject) obj.get("response");
                                JSONArray playersArray = (JSONArray) responseObject.get("players");
                                JSONObject players = (JSONObject) playersArray.get(0);
                                nickname.set(players.get("personaname") + "#" + players.get("steamid"));

                            } catch (ParseException e) {
                                log.warn("Json Parser Error :: {}", e.getMessage());
                            }

                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode rootNode;
                            try {
                                rootNode = mapper.readTree(response);
                            } catch (Exception e) {
                                return Mono.error(new Exception("Invalid JSON response"));
                            }

                            JsonNode playersNode = rootNode.path("response").path("players");
                            if (playersNode.isArray() && playersNode.isEmpty()) {
                                return Mono.error(new SteamUserNotFoundException());
                            }

                            if (response == null || response.startsWith("players")) {
                                return Mono.error(new Exception("Response is empty"));
                            }

                            return Mono.just(response);
                        });

                json.block();

                player = playerService.addSteamPlayer(key, nickname.get(), type);
            } else {
                player = playerService.addLocalPlayer(key, type);
            }

            String token = customTokenProviderService.generateToken(player);

            ResTokenDto responseDto = new ResTokenDto(token, "플레이어가 추가되었습니다.");

            log.info("플레이어 추가 성공");
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (SteamUserNotFoundException e) {
            log.warn(e.getMessage());
            log.warn("스팀 유저를 찾을 수 없음");

            ResTokenDto responseDto = new ResTokenDto(null, "스팀 유저를 찾을 수 없음");
            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            log.warn(e.getMessage());

            ResTokenDto responseDto = new ResTokenDto(null, e.getMessage());
            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        } catch (DataIntegrityViolationException e) {
            log.warn(e.getMessage());
            log.warn("중복된 닉네임 혹은 스팀 키");

            ResTokenDto responseDto = new ResTokenDto(null, "중복된 닉네임 혹은 스팀 키");
            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/avatar")
    @Operation(summary = "플레이어 아바타 추가", description = "플레이어 아바타를 추가 합니다.", responses = {
            @ApiResponse(responseCode = "201", description = "플레이어 아바타 추가 성공", content = @Content(schema = @Schema(name = "플레이어 아바타 추가 성공", example = "성공 메시지(문자열)"), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "플레이어 아바타 추가 실패", content = @Content(schema = @Schema(name = "플레이어 아바타 추가 실패", example = "에러 메시지(플레이어 정보 없음 등)"), mediaType = "application/json"))
    })
    public ResponseEntity<ResMessageDto> addPlayerAvatar(@RequestBody ReqAvatarDto reqBody) {

        String maskColor = reqBody.getMaskColor();

        try {
            log.info("플레이어 아바타 추가");
            playerService.addPlayerAvatar(maskColor);

            ResMessageDto resDto = new ResMessageDto("플레이어 아바타가 추가되었습니다.");

            log.info("플레이어 아바타 추가 성공");
            return new ResponseEntity<>(resDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.warn("플레이어 아바타 추가 실패");
            log.warn(e.getMessage());

            ResMessageDto resDto = new ResMessageDto(e.getMessage());

            return new ResponseEntity<>(resDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/avatar")
    @Operation(summary = "플레이어 아바타 변경", description = "플레이어 아바타에서 마스크 컬러를 변경 합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "플레이어 아바타 변경 성공", content = @Content(schema = @Schema(implementation = ResPlayerInfoDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "플레이어 아바타 변경 실패", content = @Content(schema = @Schema(name = "플레이어 아바타 변경 실패", example = "에러 메시지(플레이어 정보 없음 등)"), mediaType = "application/json"))
    })
    public ResponseEntity<ResPlayerInfoDto> changePlayerAvatar(@RequestBody ReqAvatarDto reqBody) {

        String maskColor = reqBody.getMaskColor();

        try {
            log.info("플레이어 아바타 변경");

            ResPlayerInfoDto resDto = playerService.changePlayerAvatar(maskColor);

            log.info("플레이어 아바타 변경 성공");
            return new ResponseEntity<>(resDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.warn("플레이어 아바타 변경 실패");
            log.warn(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/info")
    @Operation(summary = "플레이어 정보 조회", description = "플레이어 정보를 조회 합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "플레이어 정보 조회 성공", content = @Content(schema = @Schema(implementation = ResPlayerInfoDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "플레이어 정보 조회 실패", content = @Content(schema = @Schema(name = "플레이어 정보 조회 실패", example = "에러 메시지(플레이어 정보 없음 등)"), mediaType = "application/json"))
    })
    public ResponseEntity<ResPlayerInfoDto> readPlayerInfo() {

        try {
            log.info("플레이어 정보 조회");
            ResPlayerInfoDto resDto = playerService.readPlayerInfo();

            log.info("플레이어 정보 조회 완료");
            return new ResponseEntity<>(resDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.info("플레이어 정보 조회 실패");
            log.warn(e.getMessage());

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/nickname")
    @Operation(summary = "플레이어 닉네임 변경", description = "플레이어 닉네임을 수정합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "플레이어 닉네임 수정 성공", content = @Content(schema = @Schema(implementation = ResPlayerInfoDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "플레이어 닉네임 수정 실패", content = @Content(schema = @Schema(name = "플레이어 닉네임 수정 실패", example = "에러 메시지(닉네임 중복)"), mediaType = "application/json"))
    })
    @Parameter(name = "nickname", description = "변경할 닉네임", example = "test", in = ParameterIn.QUERY)
    public ResponseEntity<ResponseDto> changePlayerNickname(@RequestParam String nickname) {

        try {
            log.info("플레이어 닉네임 수정");
            ResPlayerInfoDto body = playerService.changePlayerNickname(nickname);
            ResponseDto responseDto = new ResponseDto(body, "플레이어 닉네임이 수정되었습니다.");

            log.info("플레이어 닉네임 수정 완료");
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.warn(e.getMessage());

            ResponseDto responseDto = new ResponseDto(null, e.getMessage());
            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.warn(e.getMessage());

            ResponseDto responseDto = new ResponseDto(null, "플레이어 닉네임 수정 실패");
            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/avatar")
    @Operation(summary = "플레이어 아바타 초기화", description = "플레이어 아바타를 초기화 합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "플레이어 아바타 초기화 성공", content = @Content(schema = @Schema(implementation = ResMessageDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "플레이어 아바타 초기화 실패", content = @Content(schema = @Schema(name = "플레이어 아바타 제거 실패", example = "에러 메시지(플레이어 정보 없음 등)"), mediaType = "application/json"))
    })
    public ResponseEntity<ResMessageDto> deletePlayerAvatar() {

        try {
            log.info("플레이어 아바타 초기화");

            playerService.resetPlayerInfo();
            ResMessageDto resDto = new ResMessageDto("플레이어 아바타가 초기화 되었습니다.");

            log.info("플레이어 아바타 초기화 완료");

            return new ResponseEntity<>(resDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.warn(e.getMessage());
            ResMessageDto resDto = new ResMessageDto(e.getMessage());
            return new ResponseEntity<>(resDto, HttpStatus.BAD_REQUEST);
        }
    }
}
