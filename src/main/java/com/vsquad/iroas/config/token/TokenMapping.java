package com.vsquad.iroas.config.token;

import lombok.Builder;
import lombok.Data;

@Data
public class TokenMapping {

    private String playerNickname;
    private String accessToken;

    public TokenMapping() {}

    @Builder
    public TokenMapping(String playerNickname, String accessToken) {
        this.playerNickname = playerNickname;
        this.accessToken = accessToken;
    }
}
