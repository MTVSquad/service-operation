package com.vsquad.iroas.aggregate.dto;

import com.vsquad.iroas.aggregate.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto {

    private Long playerId;

    private String playerSteamKey;

    private String playerNickName;

    private String playerType;

    private String playerRole;
}
