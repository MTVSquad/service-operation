package com.vsquad.iroas.service;

import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.repository.PlayerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PlayerServiceTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    @DisplayName("플레이어 추가")
    void addPlayerTest() {
        /*
         * 테스트 시나리오
         * 1. 스팀 회원의 식별 정보 저장
         * */

        // given
        String playerKey = "key";

        // when
        Player newPlayer = new Player();
        newPlayer.setPlayerSteamKey(playerKey);

        // 저장
        playerRepository.save(newPlayer);

        // 저장된 플레이어 조회
        Player foundPlayer = playerRepository.findByPlayerSteamKey(playerKey);

        // then
        // 조회한 값이 있는지 조회
        Assertions.assertNotNull(foundPlayer);
    }
}