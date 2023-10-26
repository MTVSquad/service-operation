package com.vsquad.iroas.aggregate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResPlayerInfoDto {

    private Long playerId;

    private String playerNickName;

    private String playerSteamKey;

    private AvatarDto avatar;
}
