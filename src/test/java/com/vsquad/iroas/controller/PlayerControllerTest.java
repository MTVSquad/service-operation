package com.vsquad.iroas.controller;

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
@Transactional
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
                        "12345678901234567",
                        "히에로스"
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
                .playerSteamKey("12345678901234567")
                .nickname(new Nickname("히에로스"))
                .build();

        playerRepository.save(player);
    }

    @AfterTransaction
    public void clear() {
        playerRepository.delete(player);
    }

    @ParameterizedTest
    @MethodSource("getPlayerInfo")
    @DisplayName("플레이어 추가 성공")
    void addPlayerTest(String steamKey, String nickname) throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/player")
                        .param("playerSteamKey", steamKey)
                        .param("playerNickName", nickname)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }
}
