package com.vsquad.iroas.service;

import com.vsquad.iroas.aggregate.dto.ReqPlayerDto;
import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.aggregate.vo.Nickname;
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
         * 2.
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

    @Test
    @DisplayName("플레이어 닉네임 추가 성공")
    void addPlayerNickname() {

        // given
        // 최소 2자, 최대 8자, 특수 문자x, 한글 / 영문
        String inputData = "아바라abcf";

        ReqPlayerDto reqPlayerDto = new ReqPlayerDto(inputData);

        Player player = new Player();

        Nickname nickname = new Nickname(reqPlayerDto.getPlayerNickName());

        player.setNickname(nickname);

        // when
        playerRepository.save(player);

        //then
        Nickname foundNickname = new Nickname(inputData);
        Player foundPlayer = playerRepository.findByNickname(foundNickname);

        assertNotNull(foundPlayer);
    }

    @Test
    @DisplayName("플레이어 닉네임 추가 실패")
    void addPlayerNicknameFail() {

        // given
        // 최소 2자, 최대 8자, 특수 문자x, 한글 / 영문
        String inputData = "라라라라라라라라랄라라";

        Player player = new Player();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            //when
            Nickname nickname = new Nickname(inputData);

            player.setNickname(nickname);

            playerRepository.save(player);

            //then
            Player foundPlayer =  playerRepository.findByNickname(nickname);
            assertNotNull(foundPlayer);

        }, "에러 출력 되지 않음...");
    }
}