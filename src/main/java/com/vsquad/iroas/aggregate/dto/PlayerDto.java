package com.vsquad.iroas.aggregate.dto;

import com.vsquad.iroas.aggregate.entity.Player;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PlayerDto {

    private Long playerId;
    private String playerKey;
    private String playerNickName;
    private String playerType;
    private String playerRole;


    public PlayerDto(Player player) {
        this.playerId = player.getPlayerId();
        this.playerKey = player.getKey();
        this.playerNickName = player.getNickname();
        this.playerType = player.getType();
        this.playerRole =  player.getPlayerRole();
    }
}
