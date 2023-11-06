package com.vsquad.iroas.controller;

import com.vsquad.iroas.aggregate.dto.PlayerDto;
import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.aggregate.vo.Nickname;
import com.vsquad.iroas.repository.PlayerRepository;
import com.vsquad.iroas.service.auth.CustomTokenProviderService;
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

import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
@Transactional
class PlayerControllerTest {

    private MockMvc mvc;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private WebApplicationContext context;

    private Player player;

    @Autowired
    private CustomTokenProviderService customTokenProviderService;

    private static Stream<Arguments> getPlayerInfo() {
        return Stream.of(
                Arguments.of(
                        "76561197960435530",
                        "테스트코드"
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
    public void accountSetup() throws Exception {
        player = Player.builder()
                .playerSteamKey("123456789012345678")
                .nickname(new Nickname("히에로스"))
                .playerRole("ROLE_PLAYER")
                .build();

        player = playerRepository.save(player);

        //Security Context에 유저정보 등록, 토큰발급
        PlayerDto playerDto = new PlayerDto(player.getPlayerId(), player.getPlayerSteamKey()
                , player.getNickname().getPlayerNickname(), player.getPlayerRole());

        String jwt = customTokenProviderService.generateToken(playerDto);

        UsernamePasswordAuthenticationToken authentication =
                customTokenProviderService.getAuthenticationById(jwt);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterTransaction
    public void clear()
    {
        playerRepository.deleteById(player.getPlayerId());
        SecurityContextHolder.clearContext();
    }

    @ParameterizedTest
    @MethodSource("getPlayerInfo")
    @DisplayName("플레이어 추가 성공 테스트")
    void addPlayerTest(String steamKey, String nickname) throws Exception {

        String requestJson = "{\"steamKey\":\"" + steamKey + "\",\"playerNickName\":\"" + nickname + "\"}";

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @ParameterizedTest
    @MethodSource("getPlayerInfo")
    @DisplayName("아바타 추가 성공 테스트")
    void addPlayerAvatarSuccessTest() throws Exception {

        String maskColor = "red";
        String requestJson = "{\"maskColor\":\"" + maskColor + "\"}";

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/player/avatar")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("플레이어 정보 조회 성공 테스트")
    void readPlayerInfoTest() throws Exception {

        addPlayerAvatarSuccessTest();

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/player/info")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("플레이어 닉네임 변경 성공 테스트")
    void changePlayerNicknameSuccessTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/player/nickname")
                        .param("nickname", "변경된닉네임")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("플레이어 닉네임 변경 실패 테스트")
    void changePlayerNicknameFailTest() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/player/nickname")
                        .param("nickname", "변경된닉네임111")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("플레이어 아바타 변경 성공 테스트")
    void changePlayerAvatarSuccessTest() throws Exception {

        addPlayerAvatarSuccessTest();

        String maskColor = "green";
        String requestJson = "{\"maskColor\":\"" + maskColor + "\"}";

        mvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/player/avatar")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("플레이어 아바타, 아이템, 돈 초기화")
    void resetPlayerInfoSuccessTest() throws Exception {

        addPlayerAvatarSuccessTest();

        String playerId =   player.getPlayerId().toString();

        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/player/avatar")
                        .param("playerId", playerId)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
