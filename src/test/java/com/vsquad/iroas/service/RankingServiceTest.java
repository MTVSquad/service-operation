package com.vsquad.iroas.service;

import com.vsquad.iroas.aggregate.dto.*;
import com.vsquad.iroas.aggregate.dto.request.ReqCreatorMapDto;
import com.vsquad.iroas.aggregate.dto.request.ReqPlayerDto;
import com.vsquad.iroas.aggregate.entity.CreatorMap;
import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.aggregate.entity.Ranking;
import com.vsquad.iroas.aggregate.vo.PlayTime;
import com.vsquad.iroas.config.exception.PlayerNotFoundException;
import com.vsquad.iroas.repository.CreatorMapRepository;
import com.vsquad.iroas.repository.PlayerRepository;
import com.vsquad.iroas.repository.RankingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RankingServiceTest {

    @Autowired
    private RankingRepository rankingRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private CreatorMapRepository creatorMapRepository;

    private boolean clearYn;

    @BeforeTransaction
    public void setup() {
        clearYn = true;
    }

    private Player addPlayer() {

        String playerKey;

        while (true) {
            playerKey = UUID.randomUUID().toString();

            Optional<Player> isDuplicated = playerRepository.findByKey(playerKey);

            if (isDuplicated.isEmpty()) {
                break;
            }
        }

        String inputData = "랭킹테스트1";

        ReqPlayerDto reqPlayerDto = new ReqPlayerDto(playerKey, inputData);

        Player player = new Player(reqPlayerDto.getKey(), "랭킹테스트1", "local", 0L, "ROLE_PLAYER");

        return playerRepository.save(player);
    }

    private CreatorMap addMap() {

        List<EnemySpawnerDto> enemySpawnerList = new ArrayList<>(List.of(
                new EnemySpawnerDto(100.00D, 160.00D, 90.00D, 100, 10D, 10D, "Melee", 100L, 10L)
        ));

        List<PropDto> propList = new ArrayList<>(List.of(
                new PropDto("prop1", "prop", 100.00D, 160.00D, 100.00D, 90.00D),
                new PropDto("prop2", "prop", 100.00D, 160.00D, 100.00D, 90.00D),
                new PropDto("prop3", "prop", 100.00D, 160.00D, 100.00D, 90.00D)
        ));

        List<Double> startPoint = new ArrayList<>(List.of(100.00D, 160.00D, 90.00D));

        ReqCreatorMapDto mapDto = new ReqCreatorMapDto("MELEE", LocalDateTime.now(),
                90.00D, 90.00D, 90.00D, 90.00D, "Morning", enemySpawnerList, propList, 1L);

        CreatorMap map = mapDto.convertToEntity(mapDto);

        // when
        creatorMapRepository.save(map);

        // then
        return creatorMapRepository.findById(map.getCreatorMapId())
                .orElseThrow(() -> new IllegalArgumentException("맵을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("랭킹 추가 성공 테스트")
    void addRankingSuccessTest() {
        // given
        // 플레이어 추가
        Player player = addPlayer();
        // 크리에이터 맵 추가
        CreatorMap map = addMap();

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime oneHourAgo = currentDateTime.minusHours(1);

        // 클리어 성공
        int clearTotalCount = 0;
        int playTotalCount = 0;
        long playElapsedTime = 30000L;

        if(clearYn) clearTotalCount++;
        playTotalCount++;

        // 플레이어 조회
        Player foundPlayer = playerRepository.findById(player.getPlayerId())
                .orElseThrow(() -> new PlayerNotFoundException("플레이어를 찾을 수 없습니다."));

        // 랭킹 추가
        Ranking ranking = new Ranking(foundPlayer, map.getCreatorMapId(),
                new PlayTime(oneHourAgo, currentDateTime, playElapsedTime), playTotalCount, clearTotalCount);

        // 페이징 정보 추가
        Pageable pageable = PageRequest.of(0, 10); // 페이징 정보 설정

        // when
        rankingRepository.findByPlayerAndCreatorMapId(foundPlayer, map.getCreatorMapId())
                .ifPresentOrElse(
                        (foundRanking) -> {
                            // 랭킹이 있으면 업데이트
                            foundRanking.setPlayTime(new PlayTime(oneHourAgo, currentDateTime, playElapsedTime));
                            foundRanking.setPlayCount(foundRanking.getPlayCount() + 1);
                            foundRanking.setClearCount(foundRanking.getClearCount() + 1);
                        },
                        () -> {
                            // 랭킹이 없으면 추가
                            rankingRepository.save(ranking);
                        }
                );

        // then
        Ranking foundRanking = rankingRepository.findByPlayerAndCreatorMapId(player, map.getCreatorMapId()).orElseThrow(
                () -> new IllegalArgumentException("랭킹을 찾을 수 없습니다.")
        );
        assertNotNull(foundRanking);
    }

    @Test
    @DisplayName("랭킹 조회 성공 테스트")
    void readRankingSuccessTest() {

        // given
        // 플레이어 추가
        Player player = addPlayer();

        // 크리에이터 맵 추가
        CreatorMap map = addMap();

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime oneHourAgo = currentDateTime.minusHours(1);

        // 클리어 성공
        int clearTotalCount = 0;
        int playTotalCount = 0;
        long playElapsedTime = 30000L;

        if(clearYn) {
            clearTotalCount++;
            playTotalCount++;
        } else {
            playTotalCount++;
        }

        // 플레이어 조회
        Player foundPlayer = playerRepository.findById(player.getPlayerId())
                .orElseThrow(() -> new IllegalArgumentException("플레이어를 찾을 수 없습니다."));

        // 랭킹 추가
        Ranking ranking = new Ranking(foundPlayer, map.getCreatorMapId(), new PlayTime(oneHourAgo, currentDateTime, playElapsedTime), playTotalCount, clearTotalCount);

        int page = 0;
        int size = 10;

        rankingRepository.findByPlayerAndCreatorMapId(foundPlayer, map.getCreatorMapId())
                .ifPresentOrElse(
                        (foundRanking) -> {
                            // 랭킹이 있으면 업데이트
                            foundRanking.setPlayTime(new PlayTime(oneHourAgo, currentDateTime, playElapsedTime));
                            foundRanking.setPlayCount(foundRanking.getPlayCount() + 1);
                            foundRanking.setClearCount(foundRanking.getClearCount() + 1);
                        },
                        () -> {
                            // 랭킹이 없으면 추가
                            rankingRepository.save(ranking);
                        }
                );

        // when
        Pageable pageable = PageRequest.of(page, size);

        Page<Ranking> foundRanking = rankingRepository.findByCreatorMapIdAndClearCountIsNot(map.getCreatorMapId(), 0, pageable);

        // then
        assertFalse(foundRanking.isEmpty());
    }

    @Test
    @DisplayName("랭킹 추가 시간 차 구하기")
    void caculateTimeSuccessTest() {
        LocalDateTime startTime = LocalDateTime.now().minusMinutes(5).minusSeconds(30);
        LocalDateTime endTime = LocalDateTime.now();

        Duration duration = Duration.between(startTime, endTime);

        long minutes = duration.toMinutes();
        long seconds = duration.getSeconds() % 60;
        long milliSec = duration.toMillis();

        String result = minutes + "분" + seconds + "초";

        System.out.println(result);

        assertEquals("5분30초", result);
    }
}