package com.vsquad.iroas.controller;

import com.vsquad.iroas.aggregate.dto.ReqPlayerDto;
import com.vsquad.iroas.aggregate.dto.ResPlayerDto;
import com.vsquad.iroas.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            @ApiResponse(responseCode = "201", description = "플레이어 추가 성공", content = @Content(schema = @Schema(implementation = ResPlayerDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "플레이어 추가 실패", content = @Content(schema = @Schema(name = "플레이어 추가 실패", example = "에러 메시지(닉네임 중복 등)"), mediaType = "application/json"))
    })
    public ResponseEntity<ResPlayerDto> addPlayer(@RequestBody ReqPlayerDto reqBody) {

        String steamKey = reqBody.getSteamKey();
        String nickname = reqBody.getPlayerNickName();

        try {
            playerService.addPlayer(steamKey, nickname);

            ResPlayerDto resDto = new ResPlayerDto("플레이어가 추가되었습니다.");
            return new ResponseEntity<>(resDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.warn(e.getMessage());

            ResPlayerDto resDto = new ResPlayerDto(e.getMessage());

            return new ResponseEntity<>(resDto, HttpStatus.BAD_REQUEST);
        }
    }
}
