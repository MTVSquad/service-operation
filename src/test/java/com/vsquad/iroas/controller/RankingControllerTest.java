package com.vsquad.iroas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vsquad.iroas.aggregate.dto.PlayerDto;
import com.vsquad.iroas.aggregate.dto.request.ReqRankingDto;
import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.aggregate.vo.Nickname;
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

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
@Transactional
class RankingControllerTest {

    private MockMvc mvc;

    @Mock
    private Player player;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private CustomTokenProviderService customTokenProviderService;

    @Autowired
    private WebApplicationContext context;

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
        PlayerDto playerDto = new PlayerDto(player);

        String jwt = customTokenProviderService.generateToken(playerDto);

        UsernamePasswordAuthenticationToken authentication =
                customTokenProviderService.getAuthenticationById(jwt);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterTransaction
    void afterTransaction() {
        playerRepository.deleteById(player.getPlayerId());
    }

    @Mock
    ReqRankingDto dto;

    @Test
    @DisplayName("랭킹 추가 성공 테스트")
    void addRankingSuccessTest() throws Exception {

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime oneHourAgo = currentDateTime.minusHours(1);
        Long playElapsedTime = 30000L;

        // 클리어 성공
        boolean clearYn = true;

        // dto 객체 생성
        dto = new ReqRankingDto(1L, oneHourAgo, currentDateTime, clearYn, playElapsedTime, player.getPlayerId());

        // dto 객체 json으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper
                .registerModule(new JavaTimeModule())
                .writeValueAsString(dto);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/ranking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }
}