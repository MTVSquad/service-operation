package com.vsquad.iroas.controller;

import com.vsquad.iroas.aggregate.dto.ReqPlayerDto;
import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.aggregate.vo.Nickname;
import com.vsquad.iroas.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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

import java.util.List;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
class PlayerControllerTest {

    private MockMvc mvc;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private WebApplicationContext context;

    private Player player;

    private static Stream<Arguments> getPlayerInfo() {
        return Stream.of(
                Arguments.of(
                        "123456789",
                        "히에로"
                    )
                );
    }

    private static Stream<Arguments> getAvatarInfo() {
        return Stream.of(
                Arguments.of(
                        "red",
                        "1L"
                    )
                );
    }

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @BeforeTransaction
    public void accountSetup() {
        player = Player.builder()
                .playerSteamKey("123456789012345678")
                .nickname(new Nickname("히에로스"))
                .build();

        player = playerRepository.save(player);
    }

    @AfterTransaction
    public void clear() {
        playerRepository.deleteById(player.getPlayerId());
    }

    @ParameterizedTest
    @MethodSource("getPlayerInfo")
    @Transactional
    @DisplayName("플레이어 추가 성공")
    void addPlayerTest(String steamKey, String nickname) throws Exception {

        String requestJson = "{\"steamKey\":\"" + steamKey + "\",\"playerNickName\":\"" + nickname + "\"}";

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/player/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("아바타 추가 성공")
    void addPlayerAvatar() throws Exception {

        Long playerId = 99L;
        String maskColor = "red";

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/player/avatar")
                        .param("playerId", String.valueOf(playerId))
                        .param("maskColor", maskColor)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("플레이어 정보 조회 성공")
    void readPlayerInfoTest() throws Exception {

        addPlayerTest("12345678901234567", "readTest");

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/player/info")
                        .param("playerId", "1")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


}
