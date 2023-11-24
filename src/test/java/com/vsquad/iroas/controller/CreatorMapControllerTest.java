package com.vsquad.iroas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vsquad.iroas.aggregate.dto.EnemySpawnerDto;
import com.vsquad.iroas.aggregate.dto.PlayerDto;
import com.vsquad.iroas.aggregate.dto.PropDto;
import com.vsquad.iroas.aggregate.dto.request.ReqCreatorMapDto;
import com.vsquad.iroas.aggregate.entity.CreatorMap;
import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.aggregate.vo.Nickname;
import com.vsquad.iroas.repository.CreatorMapRepository;
import com.vsquad.iroas.repository.PlayerRepository;
import com.vsquad.iroas.service.auth.CustomTokenProviderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
@Transactional
class CreatorMapControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

//    @Mock
    private CreatorMap creatorMap;

    @Mock
    private ReqCreatorMapDto reqCreatorMapDto;

    @Autowired
    private CreatorMapRepository creatorMapRepository;

    /////////////

    @Mock
    private Player player;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private CustomTokenProviderService customTokenProviderService;

    /////////////

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @BeforeTransaction
    void beforeTransaction() throws Exception {

        player = Player.builder()
                .key("123456789012345678")
                .nickname(new Nickname("히에로스"))
                .type("local")
                .playerRole("ROLE_PLAYER")
                .build();

        player = playerRepository.save(player);

        //Security Context에 유저정보 등록, 토큰발급
        PlayerDto playerDto = new PlayerDto(player.getPlayerId(), player.getKey()
                , player.getNickname().getPlayerNickname(), player.getType(), player.getPlayerRole());

        String jwt = customTokenProviderService.generateToken(playerDto);

        UsernamePasswordAuthenticationToken authentication =
                customTokenProviderService.getAuthenticationById(jwt);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        ////////////////////////////////////////////////////////////////////

        // given
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

        ReqCreatorMapDto mapDto = new ReqCreatorMapDto( "MELEE", LocalDateTime.now(),
                90.00D, 90.00D, 90.00D, 90.00D, "Morning", enemySpawnerList, propList);

        // 플레이어를 추가하기 그래서 임의로 수정함
        CreatorMap map = mapDto.convertToEntity(mapDto);
        map.setCreatorMapName("testNick의 맵");
        map.setCreator(player.getPlayerId());

        creatorMap = creatorMapRepository.save(map);
    }

    @AfterTransaction
    void afterTransaction() {
        playerRepository.deleteById(player.getPlayerId());
        creatorMapRepository.deleteById(creatorMap.getCreatorMapId());
    }

    @Test
    @DisplayName("크리에이터 맵 추가 성공 테스트")
    void addMapSuccessTest() throws Exception {

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

        // 맵 name(player's 맵) player를 추가해야 되서 임의로 작성함
        reqCreatorMapDto = new ReqCreatorMapDto("MELEE", LocalDateTime.now(),
                90.00D, 90.00D, 90.00D, 90.00D, "Morning", enemySpawnerList, propList);

        // dto 객체 json으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper
                .registerModule(new JavaTimeModule())
                .writeValueAsString(reqCreatorMapDto);

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
                        .param("sort", "createTime")
                        .param("direction", "asc")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("크리에이터 맵 제거 성공 테스트")
    void deleteCreatorMapSuccessTest() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/maps/{creatorMapId}", creatorMap.getCreatorMapId())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}