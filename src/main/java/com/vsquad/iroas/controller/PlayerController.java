package com.vsquad.iroas.controller;

import com.vsquad.iroas.aggregate.dto.ReqPlayerDto;
import com.vsquad.iroas.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1.0/player")
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping("/")
    @Operation(summary = "플레이어 추가", description = "스팀에서 받아온 식별 정보와 닉네임을 가지고 플레이어를 생성합니다.")
    public ResponseEntity addPlayer(@RequestBody ReqPlayerDto reqBody) {

        String steamKey = reqBody.getSteamKey();
        String nickname = reqBody.getPlayerNickName();

        try {
            playerService.addPlayer(steamKey, nickname);
            return ResponseEntity.ok("플레이어를 추가하였습니다.");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
