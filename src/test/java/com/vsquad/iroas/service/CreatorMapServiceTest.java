package com.vsquad.iroas.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vsquad.iroas.aggregate.dto.EnemySpawnerDto;
import com.vsquad.iroas.aggregate.dto.PropDto;
import com.vsquad.iroas.aggregate.dto.CreatorMapDto;
import com.vsquad.iroas.aggregate.entity.CreatorMap;
import com.vsquad.iroas.repository.CreatorMapRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CreatorMapServiceTest {

    @Autowired
    private CreatorMapRepository creatorMapRepository;

    @Mock
    private CreatorMap creatorMap;

    @BeforeTransaction
    void beforeTransaction() throws JsonProcessingException {

        // given
        // 플레이어 추가
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
                new EnemySpawnerDto(100.00D, 160.00D, 90.00D, 100, 10D, 10D, "Melee", 100L, 10L)
        ));

        List<PropDto> propList = new ArrayList<>();
        propList.addAll(List.of(
                new PropDto("prop1", "prop", 100.00D, 160.00D, 90.00D, 90.00D),
                new PropDto("prop2", "prop", 100.00D, 160.00D, 90.00D, 90.00D),
                new PropDto("prop3", "prop", 100.00D, 160.00D, 90.00D, 90.00D)
        ));

        List<Double> startPoint = new ArrayList<>();
        startPoint.addAll(List.of(100.00D, 160.00D, 90.00D));

        CreatorMapDto mapDto = new CreatorMapDto("MELEE", LocalDateTime.now(),
                90.00D, 90.00D, 90.00D, 90.00D, "Morning", enemySpawnerList, propList);

        CreatorMap map = mapDto.convertToEntity(mapDto);

        creatorMap = creatorMapRepository.save(map);
    }

    @AfterTransaction
    void afterTransaction() {
        creatorMapRepository.deleteById(creatorMap.getCreatorMapId());
    }

    @Test
    @DisplayName("크리에이터 맵 추가 성공")
    void addCreatorMapSuccessTest() throws JsonProcessingException {

        // given
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
                new EnemySpawnerDto(100.00D, 160.00D, 90.00D, 100, 10D, 10D, "Melee", 100L, 10L)
        ));

        List<PropDto> propList = new ArrayList<>();
        propList.addAll(List.of(
                new PropDto("prop1", "prop", 100.00D, 160.00D, 100.00D, 90.00D),
                new PropDto("prop2", "prop", 100.00D, 160.00D, 100.00D, 90.00D),
                new PropDto("prop3", "prop", 100.00D, 160.00D, 100.00D, 90.00D)
        ));

        List<Double> startPoint = new ArrayList<>();
        startPoint.addAll(List.of(100.00D, 160.00D, 90.00D));

        CreatorMapDto mapDto = new CreatorMapDto( "MELEE", LocalDateTime.now(),
                90.00D, 90.00D, 90.00D, 90.00D, "Morning", enemySpawnerList, propList);

        String creator = "testNick";

        CreatorMap map = mapDto.convertToEntity(mapDto);
        map.setCreatorMapId(uuid);
        map.setCreator(creator);
        map.setCreatorMapName(creator + "의 맵");

        // when
        creatorMapRepository.save(map);

        // then
        CreatorMap foundMap = creatorMapRepository.findById(map.getCreatorMapId())
                .orElseThrow(() -> new IllegalArgumentException("맵을 찾을 수 없습니다."));

        assertNotNull(foundMap);
    }

    @Test
    @DisplayName("크리에이터 맵 조회 성공")
    void readCreatorMapSuccessTest() {

        // given
        String id = creatorMap.getCreatorMapId();

        // when
        CreatorMap foundMap = creatorMapRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("맵을 찾을 수 없습니다."));

        // then
        assertNotNull(foundMap);
    }

    @DisplayName("크리에이터 맵 51개 추가 성공")
    void add51CreatorMapSuccessTest() throws JsonProcessingException {

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
                new EnemySpawnerDto(100.00D, 160.00D, 90.00D, 100, 10D, 10D, "Melee", 100L, 10L)
        ));

        List<PropDto> propList = new ArrayList<>();
        propList.addAll(List.of(
                new PropDto("prop1", "prop", 100.00D, 160.00D, 100.00D, 90.00D),
                new PropDto("prop2", "prop", 100.00D, 160.00D, 100.00D, 90.00D),
                new PropDto("prop3", "prop", 100.00D, 160.00D, 100.00D, 90.00D)
        ));

        List<Double> startPoint = new ArrayList<>();
        startPoint.addAll(List.of(100.00D, 160.00D, 90.00D));

        CreatorMapDto mapDto = new CreatorMapDto("MELEE", LocalDateTime.now(),
                90.00D, 90.00D, 90.00D, 90.00D, "Morning", enemySpawnerList, propList);

        String creator = "testNick";

        CreatorMap map = mapDto.convertToEntity(mapDto);
        map.setCreatorMapId(uuid);
        map.setCreator(creator);
        map.setCreatorMapName(creator + "의 맵");

        creatorMapRepository.save(map);
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
                new EnemySpawnerDto(100.00D, 160.00D, 90.00D, 100, 10D, 10D, "Melee", 100L, 10L)
        ));

        List<PropDto> propList = new ArrayList<>();
        propList.addAll(List.of(
                new PropDto("prop1", "prop", 100.00D, 160.00D, 100.00D, 90.00D),
                new PropDto("prop2", "prop", 100.00D, 160.00D, 100.00D, 90.00D),
                new PropDto("prop3", "prop", 100.00D, 160.00D, 100.00D, 90.00D)
        ));

        List<Double> startPoint = new ArrayList<>();
        startPoint.addAll(List.of(100.00D, 160.00D, 90.00D));

//        CreatorMapDto mapDto = new CreatorMapDto(uuid, "myAwesomeMap", "MELEE", "testNick", LocalDateTime.now(),
//                90.00D, 90.00D, 90.00D, 90.00D, "Morning", enemySpawnerList, propList);

        CreatorMapDto mapDto = new CreatorMapDto( "MELEE", LocalDateTime.now(),
                90.00D, 90.00D, 90.00D, 90.00D, "Morning", enemySpawnerList, propList);

        CreatorMap map = mapDto.convertToEntity(mapDto);
        map.setCreatorMapId(uuid);
        map.setCreatorMapName("testNick의 맵");
        map.setCreator("testNick");

        // when
        creatorMapRepository.save(map);

        // then
        CreatorMap foundMap = creatorMapRepository.findById(map.getCreatorMapId())
                .orElseThrow(() -> new IllegalArgumentException("맵을 찾을 수 없습니다."));

        return foundMap;
    }

    @Test
    @DisplayName("크리에이터 맵 목록 1페이지, 10개, 시간 순 오름 차순 조회 성공")
    void readCreatorMapListSuccessTest() throws JsonProcessingException {

        // given
        for(int i = 0; i < 51; i++) {
            add51CreatorMapSuccessTest();
        }

        int page = 0;
        int size = 10;
        String sort = "createTime";

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        // when
        Page<CreatorMap> foundMapList = creatorMapRepository.findAll(pageable);

        // then
        Assertions.assertFalse(foundMapList.isEmpty());
        assertEquals(10, foundMapList.getNumberOfElements());
    }

    @Test
    @DisplayName("크리에이터 맵 제거 성공")
    void removeMapSuccessTest() throws JsonProcessingException {

        // given
        CreatorMap addedMap = addMap();

        // when
        creatorMapRepository.deleteById(addedMap.getCreatorMapId());

        //then
        assertFalse(creatorMapRepository.findById(addedMap.getCreatorMapId()).isPresent());
    }
}