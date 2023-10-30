package com.vsquad.iroas.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vsquad.iroas.aggregate.dto.*;
import com.vsquad.iroas.aggregate.entity.CreatorMap;
import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.aggregate.entity.Ranking;
import com.vsquad.iroas.aggregate.vo.PlayTime;
import com.vsquad.iroas.repository.CreatorMapRepository;
import com.vsquad.iroas.repository.PlayerRepository;
import com.vsquad.iroas.repository.RankingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

    private Player addPlayer() {

        String playerKey;

        while (true) {
            playerKey = UUID.randomUUID().toString();

            Optional<Player> isDuplicated = playerRepository.findByPlayerSteamKey(playerKey);

            if (!isDuplicated.isPresent()) {
                break;
            }
        }

        String inputData = "랭킹테스트1";

        ReqPlayerDto reqPlayerDto = new ReqPlayerDto(playerKey, inputData);

        Player player = new Player(reqPlayerDto.getSteamKey(), reqPlayerDto.getPlayerNickName(), 0L, "ROLE_PLAYER");

        Player foundPlayer = playerRepository.save(player);

        return foundPlayer;
    }

    private CreatorMap addMap() throws JsonProcessingException {

        String uuid;

        while (true) {
            uuid = UUID.randomUUID().toString();
            Optional<CreatorMap> isCreatorMap = creatorMapRepository.findById(uuid);

            if (!isCreatorMap.isPresent()) {
                break;
            }
        }

        List<EnemySpawnerDto> enemySpawnerList = new ArrayList<>();
        enemySpawnerList.addAll(List.of(
                new EnemySpawnerDto(100.00D, 160.00D, 90.00D, 100, 10D, 10D,  "Melee", 100L, 10L)
        ));

        List<PropDto> propList = new ArrayList<>();
        propList.addAll(List.of(
                new PropDto("prop1", "prop", 100.00D, 160.00D, 100.00D, 90.00D),
                new PropDto("prop2", "prop", 100.00D, 160.00D, 100.00D, 90.00D),
                new PropDto("prop3", "prop", 100.00D, 160.00D, 100.00D, 90.00D)
        ));

        List<Double> startPoint = new ArrayList<>();
        startPoint.addAll(List.of(100.00D, 160.00D, 90.00D));

        CreatorMapDto mapDto = new CreatorMapDto(uuid, "myAwesomeMap", "MELEE", "testNick", LocalDateTime.now(),
                90.00D, 90.00D, 90.00D, 90.00D, "Morning", enemySpawnerList, propList);

        CreatorMap map = mapDto.convertToEntity(mapDto);

        // when
        creatorMapRepository.save(map);

        // then
        CreatorMap foundMap = creatorMapRepository.findById(map.getCreatorMapId())
                .orElseThrow(() -> new IllegalArgumentException("맵을 찾을 수 없습니다."));

        return foundMap;
    }

    @Test
    @DisplayName("랭킹 추가 성공 테스트")
    void addRankingSuccessTest() throws JsonProcessingException {
        // given
        // 플레이어 추가
        Player player = addPlayer();

        // 크리에이터 맵 추가
        CreatorMap map = addMap();

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime oneHourAgo = currentDateTime.minusHours(1);

        // 클리어 성공
        boolean clearYn = true;
        int clearTotalCount = 0;
        int playTotalCount = 0;

        if(clearYn) {
            clearTotalCount++;
            playTotalCount++;
        } else {
            playTotalCount++;
        }

        // 랭킹 추가
        Ranking ranking = new Ranking(player.getPlayerId(), map.getCreatorMapId(), new PlayTime(oneHourAgo, currentDateTime), playTotalCount, clearTotalCount);

        // when
        rankingRepository.findByPlayerIdAndCreatorMapId(player.getPlayerId(), map.getCreatorMapId())
                .ifPresentOrElse(
                        (foundRanking) -> {
                            // 랭킹이 있으면 업데이트
                            foundRanking.setPlayTime(new PlayTime(oneHourAgo, currentDateTime));
                            foundRanking.setPlayCount(foundRanking.getPlayCount() + 1);
                            foundRanking.setClearCount(foundRanking.getClearCount() + 1);
                        },
                        () -> {
                            // 랭킹이 없으면 추가
                            rankingRepository.save(ranking);
                        }
                );

        // then
        List<Ranking> foundRanking = rankingRepository.findByCreatorMapId(map.getCreatorMapId());
        assertFalse(foundRanking.isEmpty());
    }

}