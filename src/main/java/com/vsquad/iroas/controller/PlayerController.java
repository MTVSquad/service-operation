package com.vsquad.iroas.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsquad.iroas.aggregate.dto.*;
import com.vsquad.iroas.aggregate.dto.request.ReqAvatarDto;
import com.vsquad.iroas.aggregate.dto.request.ReqPlayerDto;
import com.vsquad.iroas.aggregate.dto.response.*;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/player")
@Slf4j
@Tag(name = "플레이어 API")
public class PlayerController {

    private final PlayerService playerService;

    private final CustomTokenProviderService customTokenProviderService;

    @Value("${steam.api.key}")
    private String steamApiKey;

    private final WebClient webClient;

    private final ObjectMapper objectMapper;

    public PlayerController(PlayerService playerService, CustomTokenProviderService customTokenProviderService, WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("http://api.steampowered.com").build();
        this.playerService = playerService;
        this.customTokenProviderService = customTokenProviderService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "스팀에서 받아온 식별 정보를 가지고 로그인 합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "로그인 실패", content = @Content(schema = @Schema(implementation = ResponseError.class), mediaType = "application/json"))
    })
    public ResponseEntity<ResponseDto<?>> steamLogin(@RequestBody ReqPlayerDto reqPlayer) {
        log.info("로그인 시도");

        String key = reqPlayer.getKey();
        String type = reqPlayer.getType();

        PlayerDto player = playerService.readPlayer(key, type);

        String token = customTokenProviderService.generateToken(player);

        String nickname = player.getPlayerNickName();

        ResTokenDto body = new ResTokenDto(token, nickname);

        log.info("로그인 성공");
        return ResponseEntity.ok(ResponseDto.success(body, "로그인 성공"));

    }

    @PostMapping
    @Operation(summary = "플레이어 추가", description = "스팀에서 받아온 식별 정보와 닉네임을 가지고 플레이어를 생성 합니다.", responses = {
            @ApiResponse(responseCode = "201", description = "플레이어 추가 성공", content = @Content(schema = @Schema(implementation = ResTokenDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "플레이어 추가 실패", content = @Content(schema = @Schema(implementation = ResponseError.class), mediaType = "application/json"))
    })
    public ResponseEntity<ResponseDto<?>> addPlayer(@RequestBody ReqPlayerDto reqBody) {
        log.info("플레이어 추가 시도");

        String key = reqBody.getKey();
        String type = reqBody.getType();

        if (key == null || key.isEmpty()) {
            throw new PlayerNotFoundException("회원 식별 정보가 없습니다.");
        }

        PlayerDto player;
        if ("steam".equals(type)) {
            String nickname = fetchSteamNickname(key);
            player = playerService.addSteamPlayer(key, nickname, type);
        } else {
            player = playerService.addLocalPlayer(key, type);
        }

        String token = customTokenProviderService.generateToken(player);
        ResTokenDto body = new ResTokenDto(token, player.getPlayerNickName());

        log.info("플레이어 추가 성공");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success(body, "플레이어 추가 성공"));
    }

    // 스팀 닉네임 조회 메서드
    private String fetchSteamNickname(String steamKey) {
        Mono<String> json = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/ISteamUser/GetPlayerSummaries/v0002/")
                        .queryParam("key", steamApiKey)
                        .queryParam("steamids", steamKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class);

        String response = json.block();
        log.info("스팀 응답 값 {}", response);

        return parseSteamResponseForNickname(response);
    }

    // 닉네임 꺼내기
    private String parseSteamResponseForNickname(String response) {
        JsonNode rootNode = parseJson(response)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 JSON 응답입니다."));

        JsonNode playersArray = rootNode.path("response").path("players");

        if (!playersArray.isArray() || playersArray.isEmpty()) {
            throw new SteamUserNotFoundException("스팀 유저를 찾을 수 없음");
        }

        return playersArray.get(0).path("personaname").asText();
    }

    // JSON 파싱
    private Optional<JsonNode> parseJson(String response) {
        try {
            return Optional.of(objectMapper.readTree(response));
        } catch (IOException e) {
            log.warn("JSON 파싱 오류 :: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @PostMapping("/avatar")
    @Operation(summary = "플레이어 아바타 추가", description = "플레이어 아바타를 추가 합니다.", responses = {
            @ApiResponse(responseCode = "201", description = "플레이어 아바타 추가 성공", content = @Content(schema = @Schema(name = "플레이어 아바타 추가 성공", example = "성공 메시지(문자열)"), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "플레이어 아바타 추가 실패", content = @Content(schema = @Schema(name = "플레이어 아바타 추가 실패", example = "에러 메시지(플레이어 정보 없음 등)"), mediaType = "application/json"))
    })
    public ResponseEntity<ResponseDto<String>> addPlayerAvatar(@RequestBody ReqAvatarDto reqBody) {
        log.info("플레이어 아바타 추가 시도");

        String maskColor = reqBody.getMaskColor();

        playerService.addPlayerAvatar(maskColor);

        log.info("플레이어 아바타 추가 성공");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success("플레이어 아바타가 추가되었습니다."));
    }

    @PatchMapping("/avatar")
    @Operation(summary = "플레이어 아바타 변경", description = "플레이어 아바타에서 마스크 컬러를 변경 합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "플레이어 아바타 변경 성공", content = @Content(schema = @Schema(implementation = ResPlayerInfoDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "플레이어 아바타 변경 실패", content = @Content(schema = @Schema(name = "플레이어 아바타 변경 실패", example = "에러 메시지(플레이어 정보 없음 등)"), mediaType = "application/json"))
    })
    public ResponseEntity<ResponseDto<?>> changePlayerAvatar(@RequestBody ReqAvatarDto reqBody) {
        log.info("플레이어 아바타 변경 시도");

        String maskColor = reqBody.getMaskColor();

        ResPlayerInfoDto body = playerService.changePlayerAvatar(maskColor);

        log.info("플레이어 아바타 변경 성공");
        return ResponseEntity.ok(ResponseDto.success(body, "플레이어 아바타 변경 성공"));
    }

    @GetMapping("/info")
    @Operation(summary = "플레이어 정보 조회", description = "플레이어 정보를 조회 합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "플레이어 정보 조회 성공", content = @Content(schema = @Schema(implementation = ResPlayerInfoDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "플레이어 정보 조회 실패", content = @Content(schema = @Schema(implementation = ResponseError.class), mediaType = "application/json"))
    })
    public ResponseEntity<ResponseDto<?>> readPlayerInfo() {
        log.info("플레이어 정보 조회 시도");
        ResPlayerInfoDto body = playerService.readPlayerInfo();

        log.info("플레이어 정보 조회 완료");
        return ResponseEntity.ok(ResponseDto.success(body, "플레이어 정보 조회"));
    }

    @PatchMapping("/nickname")
    @Operation(summary = "플레이어 닉네임 변경", description = "플레이어 닉네임을 수정합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "플레이어 닉네임 수정 성공", content = @Content(schema = @Schema(implementation = ResPlayerInfoDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "플레이어 닉네임 수정 실패", content = @Content(schema = @Schema(implementation = ResponseError.class), mediaType = "application/json"))
    })
    @Parameter(name = "nickname", description = "변경할 닉네임", example = "test", in = ParameterIn.QUERY)
    public ResponseEntity<ResponseDto<?>> changePlayerNickname(@RequestParam String nickname) {
        log.info("플레이어 닉네임 수정 시도");

        ResPlayerInfoDto body = playerService.changePlayerNickname(nickname);
        log.info("플레이어 닉네임 수정 완료");

        return ResponseEntity.ok(ResponseDto.success(body, "플레이어 닉네임이 수정되었습니다."));
    }

    @DeleteMapping("/avatar")
    @Operation(summary = "플레이어 아바타 초기화", description = "플레이어 아바타를 초기화 합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "플레이어 아바타 초기화 성공", content = @Content(schema = @Schema(implementation = ResMessageDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "플레이어 아바타 초기화 실패", content = @Content(schema = @Schema(implementation = ResponseError.class), mediaType = "application/json"))
    })
    public ResponseEntity<ResponseDto<String>> deletePlayerAvatar() {
        log.info("플레이어 아바타 초기화 시도");

        playerService.resetPlayerInfo();

        log.info("플레이어 아바타 초기화 성공");

        return ResponseEntity.ok(ResponseDto.success("플레이어 아바타가 초기화 되었습니다."));
    }
}
