package com.vsquad.iroas.aggregate.dto;

import com.vsquad.iroas.aggregate.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResPlayerInfoDto {

    private Long playerId;

    private String playerSteamKey;

    private String playerNickName;

    private Long avatarId;

    private String playerRole;

    public static ResPlayerInfoDto convertToDto(Player player) {

        return new ResPlayerInfoDto(player.getPlayerId(), player.getPlayerSteamKey(), player.getNickname().getPlayerNickname(),
                player.getPlayerAvatar(), player.getPlayerRole());
    }
}
