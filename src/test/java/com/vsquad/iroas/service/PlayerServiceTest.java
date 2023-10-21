package com.vsquad.iroas.service;

import com.vsquad.iroas.aggregate.dto.ReqPlayerDto;
import com.vsquad.iroas.aggregate.entity.Avatar;
import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.aggregate.vo.Nickname;
import com.vsquad.iroas.repository.AvatarRepository;
import com.vsquad.iroas.repository.PlayerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NonUniqueResultException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PlayerServiceTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private AvatarRepository avatarRepository;

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
        Player foundPlayer = playerRepository.findByPlayerSteamKey(playerKey).orElseThrow();

        // then
        // 조회한 값이 있는지 조회
        Assertions.assertNotNull(foundPlayer);
    }

    @Test
    @DisplayName("플레이어 닉네임 추가 성공")
    void addPlayerNicknameTest() {

        // given
        String playerKey = "key";
        // 최소 2자, 최대 8자, 특수 문자x, 한글 / 영문
        String inputData = "아바라abcf";

        ReqPlayerDto reqPlayerDto = new ReqPlayerDto(playerKey, inputData);

        Player player = new Player();

        Nickname nickname = new Nickname(reqPlayerDto.getPlayerNickName());

        player.setNickname(nickname);

        // when
        playerRepository.save(player);

        //then
        Nickname foundNickname = new Nickname(inputData);
        Player foundPlayer = playerRepository.findByNickname(foundNickname).orElseThrow();

        assertNotNull(foundPlayer);
    }

    @Test
    @DisplayName("플레이어 닉네임 추가 실패, 글자 수 8자 이상")
    void addPlayerNicknameFailTest() {

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
            Player foundPlayer =  playerRepository.findByNickname(nickname).orElseThrow();
            assertNotNull(foundPlayer);

        }, "에러 출력 되지 않음...");
    }

    @Test
    @DisplayName("플레이어 닉네임 추가 실패, 닉네임 중복")
    void addPlayerDuplicatedNicknameFailTest() {

        // given
        // 최소 2자, 최대 8자, 특수 문자x, 한글 / 영문
        String inputNickname = "테스트닉네임";
        String steamKey1 = "testKey";

        String inputNickname2 = "테스트닉네임";
        String steamKey2 = "testKey2";

        Player player1 = new Player(steamKey1, inputNickname);
        playerRepository.save(player1);


        // then
        // unique constraint 위반으로 에러 출력 되지만, DataIntegrityViolationException은 닉네임, 스팀, 다른 에러를 구분할 수 없음
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {

            playerRepository.findByNickname(new Nickname(inputNickname)).orElseThrow();

            Player player2 = new Player(steamKey2, inputNickname2);
            playerRepository.save(player2);

        }, "에러 출력 되지 않음...");
    }

    @Test
    @DisplayName("플레이어 닉네임 변경 성공")
    void changePlayerNicknameSuccessTest() {

        // given
        String playerKey = "key";
        // 최소 2자, 최대 8자, 특수 문자x, 한글 / 영문
        String inputData = "아바라abcf";
        ReqPlayerDto reqPlayerDto = new ReqPlayerDto(playerKey, inputData);
        Player player = new Player();
        Nickname nickname = new Nickname(reqPlayerDto.getPlayerNickName());
        player.setNickname(nickname);
        playerRepository.save(player);

        // when
        Nickname foundNickname = new Nickname(inputData);
        Player foundPlayer = playerRepository.findByNickname(foundNickname).orElseThrow();

        Nickname nicknameToChange = new Nickname("바닐라라떼");
        foundPlayer.setNickname(nicknameToChange);
        playerRepository.save(foundPlayer);

        // then
        Player changedPlayer =  playerRepository.findByNickname(nicknameToChange).orElseThrow();
        String changedNickname = changedPlayer.getNickname().getPlayerNickname();

        assertEquals("바닐라라떼", changedNickname);
    }

    @Test
    @DisplayName("닉네임 변경 실패")
    void changePlayerNicknameFailTest() {

        // given
        String playerKey = "key";
        String inputData = "바닐라라떼";
        ReqPlayerDto reqPlayerDto = new ReqPlayerDto(playerKey, inputData);
        Player player = new Player();
        Nickname nickname = new Nickname(reqPlayerDto.getPlayerNickName());
        player.setNickname(nickname);
        playerRepository.save(player);

        // when
        Nickname foundNickname = new Nickname(inputData);
        Player foundPlayer = playerRepository.findByNickname(foundNickname).orElseThrow();

        Nickname nicknameToChange = new Nickname("바닐라라떼");
        foundPlayer.setNickname(nicknameToChange);
        playerRepository.save(foundPlayer);

        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {

            //when
            Nickname changedNickname = new Nickname("unlimitedPower");

            player.setNickname(changedNickname);

            playerRepository.save(player);

            //then
            Player changedPlayer =  playerRepository.findByNickname(changedNickname).orElseThrow();
            assertNotNull(changedPlayer);

        }, "에러 출력 되지 않음...");
    }

    @Test
    @DisplayName("아바타 추가 성공")
    void addPlyerAvatarTest() {

        // given
        // 플레이어 추가
        String playerKey = "key";
        Player newPlayer = new Player();
        newPlayer.setPlayerSteamKey(playerKey);
        Player savedPlayer = playerRepository.save(newPlayer);

        // 플레이어 아바타 추가
        Long playerId = savedPlayer.getPlayerId();
        String playerMaskColor = "red";

        // when
        Avatar avatar = new Avatar(playerId, playerMaskColor);
        avatarRepository.save(avatar);

        // then
        Avatar foundAvatar = avatarRepository.findByPlayerId(playerId).orElseThrow(() -> {
            throw new IllegalArgumentException("플레이어 정보가 일치하지 않습니다.");
        });

        assertNotNull(foundAvatar);
    }

    @Test
    @DisplayName("아바타 커스텀 변경 성공")
    void changePlayerAvatarTest() {

        // given
        // 플레이어 추가
        String playerKey = "key";
        Player newPlayer = new Player();
        newPlayer.setPlayerSteamKey(playerKey);
        Player savedPlayer = playerRepository.save(newPlayer);

        // 플레이어 아바타 추가
        Long playerId = savedPlayer.getPlayerId();
        String playerMaskColor = "red";
        Avatar avatar = new Avatar(playerId, playerMaskColor);
        avatarRepository.save(avatar);

        // when
        Avatar foundAvatar = avatarRepository.findByPlayerId(playerId).orElseThrow(() -> {
            throw new IllegalArgumentException("플레이어 정보가 일치하지 않습니다.");
        });
        foundAvatar.setMask("blue");
        avatarRepository.save(foundAvatar);

        // then
        Avatar changedAvatar =  avatarRepository.findByPlayerId(playerId).orElseThrow(() -> {
            throw new IllegalArgumentException("저장된 플레이어가 없습니다.");
        });

        assertEquals("blue", changedAvatar.getMask());
    }

    @Test
    @DisplayName("회원 정보 초기화")
    void resetPlayerInfo() {

        // given
        // 플레이어 추가
        String playerKey = "key";
        Player newPlayer = new Player();
        newPlayer.setPlayerSteamKey(playerKey);
        Player savedPlayer = playerRepository.save(newPlayer);

        // 플레이어 아바타 추가
        Long playerId = savedPlayer.getPlayerId();
        String playerMaskColor = "red";
        Avatar avatar = new Avatar(playerId, playerMaskColor);
        Avatar registedAvatar = avatarRepository.save(avatar);

        savedPlayer.setPlayerAvatar(registedAvatar.getAvatarId());
        playerRepository.save(savedPlayer);

        // when
        Player foundPlayer = playerRepository.findById(savedPlayer.getPlayerId()).orElseThrow(() -> {
            throw new IllegalArgumentException("저장된 플레이어가 없습니다.");
        });

        avatarRepository.deleteById(foundPlayer.getPlayerAvatar());

        foundPlayer.setPlayerItems(null);
        foundPlayer.setPlayerMoney(0L);
        foundPlayer.setPlayerAvatar(null);
        playerRepository.save(foundPlayer);

        // then
        Player changedPlayer = playerRepository.findById(foundPlayer.getPlayerId()).orElseThrow(() -> {
            throw new IllegalArgumentException("저장된 플레이어가 없습니다.");
        });

        Assertions.assertThrows(NoSuchElementException.class, () -> {
           avatarRepository.findByPlayerId(foundPlayer.getPlayerId()).orElseThrow();
        }, "에러 출력 되지 않음...");

        assertNull(changedPlayer.getPlayerAvatar());
        assertEquals(null, changedPlayer.getPlayerItems());
        assertEquals(0L, changedPlayer.getPlayerMoney());
    }

    @Test
    @DisplayName("플레이어 정보 조회 성공")
    void readPlayerInfSuccessTest() {

        // given
        String playerKey = "key";

        Player newPlayer = new Player();
        newPlayer.setPlayerSteamKey(playerKey);

        Player savedPlayer = playerRepository.save(newPlayer);

        // 플레이어 아바타 추가
        Long playerId = savedPlayer.getPlayerId();
        String playerMaskColor = "red";

        Avatar avatar = new Avatar(playerId, playerMaskColor);
        avatarRepository.save(avatar);

        // when
        Player foundPlayer = playerRepository.findById(savedPlayer.getPlayerId()).orElseThrow(() -> {
            throw new IllegalArgumentException("저장된 플레이어가 없습니다.");
        });

        // then
        assertNotNull(foundPlayer);
    }

    @Test
    @DisplayName("중복된 닉네임 체크")
    void duplicatedNicknameCheckTest() {

            // given
            String playerKey = "key";
            String nickname = "히에로스";

            Player player1 = new Player(playerKey, nickname);
            playerRepository.save(player1);

            Player player2 = new Player(playerKey, nickname);
            playerRepository.save(player2);

            // when
            Nickname testNickname = new Nickname("히에로스");
            Player foundPlayer = playerRepository.findByNickname(testNickname).orElseThrow(() -> {
                throw new IllegalArgumentException("중복된 닉네임 입니다.");
            });

            // then
            assertNotNull(foundPlayer);
    }

    @Test
    @DisplayName("회원 정보 조회 실패, 닉네임 불일치")
    void readPlayerInfoFailTest() {

            // given
            String playerKey = "key";
            String nickname = "히에로스";

            Player newPlayer = new Player(playerKey, nickname);

            Player savedPlayer = playerRepository.save(newPlayer);

            // 플레이어 아바타 추가
            Long playerId = savedPlayer.getPlayerId();
            String playerMaskColor = "red";

            Avatar avatar = new Avatar(playerId, playerMaskColor);
            avatarRepository.save(avatar);

            // then
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                Nickname testNickname = new Nickname("테스트1");
                playerRepository.findByNickname(testNickname).orElseThrow(() -> {
                    throw new IllegalArgumentException("저장된 플레이어가 없습니다.");
                });
            }, "에러 출력 되지 않음...");


    }
}