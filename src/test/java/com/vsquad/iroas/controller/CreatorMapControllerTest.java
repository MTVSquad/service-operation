package com.vsquad.iroas.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vsquad.iroas.aggregate.dto.EnemyDto;
import com.vsquad.iroas.aggregate.dto.EnemySpawnerDto;
import com.vsquad.iroas.aggregate.dto.PropDto;
import com.vsquad.iroas.aggregate.dto.CreatorMapDto;
import com.vsquad.iroas.aggregate.entity.CreatorMap;
import com.vsquad.iroas.repository.CreatorMapRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
@Transactional
class CreatorMapControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    private CreatorMap creatorMap;

    @Mock
    private CreatorMapDto reqCreatorMapDto;

    @Autowired
    private CreatorMapRepository creatorMapRepository;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @BeforeTransaction
    void beforeTransaction() throws JsonProcessingException {

        // given
        String uuid = UUID.randomUUID().toString();

        EnemyDto enemyDto = new EnemyDto("close_range_0", "근거리1", "Melee", 100L, 10L);

        List<EnemySpawnerDto> enemySpawnerList = new ArrayList<>();
        enemySpawnerList.addAll(List.of(
                new EnemySpawnerDto("근접 에네미 스포너", 100.00D, 160.00D, 90.00D, 100, 10D, 10D, enemyDto)
        ));

        List<PropDto> propList = new ArrayList<>();
        propList.addAll(List.of(
                new PropDto("prop1", "prop", 100.00D, 160.00D, 90.00D, 90.00D),
                new PropDto("prop2", "prop", 100.00D, 160.00D, 90.00D, 90.00D),
                new PropDto("prop3", "prop", 100.00D, 160.00D, 90.00D, 90.00D)
        ));

        List<Double> startPoint = new ArrayList<>();
        startPoint.addAll(List.of(100.00D, 160.00D, 90.00D));

        CreatorMapDto mapDto = new CreatorMapDto(uuid, "testMap", "MELEE", 1L, LocalDateTime.now(),
                90.00D, 90.00D, 90.00D, 90.00D, "Morning", enemySpawnerList, propList);

        CreatorMap map = mapDto.convertToEntity(mapDto);

        creatorMap = creatorMapRepository.save(map);
    }

    @AfterTransaction
    void afterTransaction() {
        creatorMapRepository.deleteById(creatorMap.getCreatorMapId());
    }

    @Test
    @DisplayName("크리에이터 맵 추가 성공 테스트")
    void addMapSuccessTest() throws Exception {

        // given
        String uuid = UUID.randomUUID().toString();

        EnemyDto enemyDto = new EnemyDto("close_range_1", "근거리1", "Melee", 100L, 10L);

        List<EnemySpawnerDto> enemySpawnerList = new ArrayList<>();
        enemySpawnerList.addAll(List.of(
                new EnemySpawnerDto("근접 에네미 스포너", 100.00D, 160.00D, 90.00D, 100, 10D, 10D, enemyDto)
        ));

        List<PropDto> propList = new ArrayList<>();
        propList.addAll(List.of(
                new PropDto("prop1", "prop", 100.00D, 160.00D, 90.00D, 90.00D),
                new PropDto("prop2", "prop", 100.00D, 160.00D, 90.00D, 90.00D),
                new PropDto("prop3", "prop", 100.00D, 160.00D, 90.00D, 90.00D)
        ));

        List<Double> startPoint = new ArrayList<>();
        startPoint.addAll(List.of(100.00D, 160.00D, 90.00D));

        reqCreatorMapDto = new CreatorMapDto(uuid, "myAwesomeMap", "MELEE", 1L, LocalDateTime.now(),
                90.00D, 90.00D, 90.00D, 90.00D,  "Morning", enemySpawnerList, propList);

        // dto 객체 json으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper
                        .registerModule(new JavaTimeModule())
                        .writeValueAsString(reqCreatorMapDto);
        System.out.println("json = " + json);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/maps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("크리에이터 맵 조회 성공 테스트")
    void readCreatorMap() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/maps/{creatorMapId}", creatorMap.getCreatorMapId())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    @DisplayName("크리에이터 맵 목록 조회 성공 테스트")
    void readCreatorMapPageSuccessTest() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/maps")
                        .param("page", "0")
                        .param("size", "10")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}