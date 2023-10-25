package com.vsquad.iroas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vsquad.iroas.aggregate.dto.EnemyDto;
import com.vsquad.iroas.aggregate.dto.EnemySpawnerDto;
import com.vsquad.iroas.aggregate.dto.PropDto;
import com.vsquad.iroas.aggregate.dto.ReqCreatorMapDto;
import com.vsquad.iroas.aggregate.entity.CreatorMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
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

import static org.junit.jupiter.api.Assertions.*;
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
    private ReqCreatorMapDto reqCreatorMapDto;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("크리에이터 맵 추가 성공 테스트")
    void addMapSuccessTest() throws Exception {

        // given
        String uuid = UUID.randomUUID().toString();

        EnemyDto enemyDto = new EnemyDto("close_range_1", "근거리1", "Melee", 100L, 10L);

        List<Float> enemyStartPoint1 = new ArrayList<>();
        enemyStartPoint1.addAll(List.of(100.00F, 160.00F, 90.00F));

        List<EnemySpawnerDto> enemySpawnerList = new ArrayList<>();
        enemySpawnerList.addAll(List.of(
                new EnemySpawnerDto("근접 에네미 스포너", enemyStartPoint1, 100, 10F, 10F, enemyDto)
        ));

        List<PropDto> propList = new ArrayList<>();
        propList.addAll(List.of(
                new PropDto("prop1", "prop", List.of(100.00F, 160.00F, 90.00F)),
                new PropDto("prop2", "prop", List.of(100.00F, 160.00F, 90.00F)),
                new PropDto("prop3", "prop", List.of(100.00F, 160.00F, 90.00F))
        ));

        List<Float> startPoint = new ArrayList<>();
        startPoint.addAll(List.of(100.00F, 160.00F, 90.00F));

        reqCreatorMapDto = new ReqCreatorMapDto(uuid, "myAwesomeMap", "MELEE", 1L, LocalDateTime.now(),
                startPoint, "Morning", enemySpawnerList, propList);

        // dto 객체 json으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper
                        .registerModule(new JavaTimeModule())
                        .writeValueAsString(reqCreatorMapDto);
        System.out.println("json = " + json);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/map")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }
}