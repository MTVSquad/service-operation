package com.vsquad.iroas.controller;

import com.vsquad.iroas.aggregate.dto.ReqAvatarDto;
import com.vsquad.iroas.aggregate.dto.ReqPlayerDto;
import com.vsquad.iroas.aggregate.dto.ResMessageDto;
import com.vsquad.iroas.aggregate.dto.ResPlayerInfoDto;
import com.vsquad.iroas.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/player")
@Slf4j
@Tag(name = "플레이어 API")
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/login/steam")
    public String steamLogin() {
        return "Redirecting to Steam for authentication...";
    }

    @GetMapping("/login/success")
    public String loginSuccess() {
        return "Successfully authenticated with Steam!";
    }

    @PostMapping("/add")
    @Operation(summary = "플레이어 추가", description = "스팀에서 받아온 식별 정보와 닉네임을 가지고 플레이어를 생성 합니다.", responses = {
            @ApiResponse(responseCode = "201", description = "플레이어 추가 성공", content = @Content(schema = @Schema(implementation = ResMessageDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "플레이어 추가 실패", content = @Content(schema = @Schema(name = "플레이어 추가 실패", example = "에러 메시지(닉네임 중복 등)"), mediaType = "application/json"))
    })
    public ResponseEntity<ResMessageDto> addPlayer(@RequestBody ReqPlayerDto reqBody) {

        String steamKey = reqBody.getSteamKey();
        String nickname = reqBody.getPlayerNickName();

        try {

            if(steamKey == null || steamKey.isEmpty()) {
                throw new IllegalArgumentException("스팀에서 받아온 회원 식별 정보가 없습니다.");
            }

            playerService.addPlayer(steamKey, nickname);

            ResMessageDto resDto = new ResMessageDto("플레이어가 추가되었습니다.");
            return new ResponseEntity<>(resDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.warn(e.getMessage());

            ResMessageDto resDto = new ResMessageDto(e.getMessage());

            return new ResponseEntity<>(resDto, HttpStatus.BAD_REQUEST);
        } catch (DataIntegrityViolationException e) {
            log.warn(e.getMessage());

            ResMessageDto resDto = new ResMessageDto("중복된 닉네임 혹은 스팀 키");

            return new ResponseEntity<>(resDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/avatar")
    @Operation(summary = "플레이어 아바타 추가", description = "플레이어 아바타를 추가 합니다.", responses = {
            @ApiResponse(responseCode = "201", description = "플레이어 아바타 추가 성공", content = @Content(schema = @Schema(name = "플레이어 아바타 추가 성공", example = "성공 메시지(문자열)"), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "플레이어 아바타 추가 실패", content = @Content(schema = @Schema(name = "플레이어 아바타 추가 실패", example = "에러 메시지(플레이어 정보 없음 등)"), mediaType = "application/json"))
    })
    public ResponseEntity<ResMessageDto> addPlayerAvatar(@RequestBody ReqAvatarDto reqBody) {

        Long playerId = reqBody.getPlayerId();
        String maskColor = reqBody.getMaskColor();

        try {
            playerService.addPlayerAvatar(playerId, maskColor);

            ResMessageDto resDto = new ResMessageDto("플레이어 아바타가 추가되었습니다.");
            return new ResponseEntity<>(resDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
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

        Long playerId = reqBody.getPlayerId();
        String maskColor = reqBody.getMaskColor();

        try {
            ResPlayerInfoDto resDto = playerService.changePlayerAvatar(playerId, maskColor);
            return new ResponseEntity<>(resDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/info")
    @Operation(summary = "플레이어 정보 조회", description = "플레이어 정보를 조회 합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "플레이어 정보 조회 성공", content = @Content(schema = @Schema(implementation = ResPlayerInfoDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "플레이어 정보 조회 실패", content = @Content(schema = @Schema(name = "플레이어 정보 조회 실패", example = "에러 메시지(플레이어 정보 없음 등)"), mediaType = "application/json"))
    })
    public ResponseEntity<ResPlayerInfoDto> readPlayerInfo(@RequestParam Long playerId) {

        try {
            ResPlayerInfoDto resDto = playerService.readPlayerInfo(playerId);
            return new ResponseEntity<>(resDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.warn(e.getMessage());

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/nickname")
    @Operation(summary = "플레이어 닉네임 변경", description = "플레이어 닉네임을 수정합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "플레이어 닉네임 수정 성공", content = @Content(schema = @Schema(implementation = ResPlayerInfoDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "플레이어 닉네임 수정 실패", content = @Content(schema = @Schema(name = "플레이어 닉네임 수정 실패", example = "에러 메시지(닉네임 중복)"), mediaType = "application/json"))
    })
    @Parameters({
        @Parameter(name = "playerId", description = "플레이어 식별자", example = "1", in = ParameterIn.QUERY),
        @Parameter(name = "nickname", description = "플레이어 닉네임", example = "test", in = ParameterIn.QUERY)
    })
    public ResponseEntity<ResPlayerInfoDto> changePlayerNickname(@RequestParam Long playerId, @RequestParam String nickname) {

        try {
            log.info("플레이어 닉네임 수정");
            ResPlayerInfoDto resDto = playerService.changePlayerNickname(playerId, nickname);
            return new ResponseEntity<>(resDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/avatar")
    @Operation(summary = "플레이어 아바타 초기화", description = "플레이어 아바타를 초기화 합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "플레이어 아바타 초기화 성공", content = @Content(schema = @Schema(implementation = ResMessageDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "플레이어 아바타 초기화 실패", content = @Content(schema = @Schema(name = "플레이어 아바타 제거 실패", example = "에러 메시지(플레이어 정보 없음 등)"), mediaType = "application/json"))
    })
    @Parameter(name = "playerId", description = "플레이어 식별자", example = "1", in = ParameterIn.QUERY)
    public ResponseEntity<ResMessageDto> deletePlayerAvatar(@RequestParam Long playerId) {

        try {
            log.info("플레이어 아바타 제거");

            playerService.resetPlayerInfo(playerId);
            ResMessageDto resDto = new ResMessageDto("플레이어 아바타가 초기화 되었습니다.");
            return new ResponseEntity<>(resDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.warn(e.getMessage());
            ResMessageDto resDto = new ResMessageDto(e.getMessage());
            return new ResponseEntity<>(resDto, HttpStatus.BAD_REQUEST);
        }
    }
}
