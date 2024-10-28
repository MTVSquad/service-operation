package com.vsquad.iroas.config;

import com.vsquad.iroas.aggregate.dto.PlayerDto;
import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.aggregate.vo.Nickname;
import com.vsquad.iroas.service.auth.CustomTokenProviderService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.transaction.BeforeTransaction;

@TestConfiguration
public class TestConfig {

    @Mock
    private Player player;

    @Autowired
    private CustomTokenProviderService customTokenProviderService;

    @BeforeEach
    public void accountSetup() throws Exception {
        player = Player.builder()
                .key("123456789012345678")
                .nickname(new Nickname("히에로스"))
                .type("local")
                .playerRole("ROLE_PLAYER")
                .build();

        //Security Context에 유저정보 등록, 토큰발급
        PlayerDto playerDto = new PlayerDto(player);

        String jwt = customTokenProviderService.generateToken(playerDto);

        UsernamePasswordAuthenticationToken authentication =
                customTokenProviderService.getAuthenticationById(jwt);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
