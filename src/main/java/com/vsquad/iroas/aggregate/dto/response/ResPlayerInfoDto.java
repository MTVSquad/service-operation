package com.vsquad.iroas.aggregate.dto.response;

import com.vsquad.iroas.aggregate.entity.Player;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResPlayerInfoDto {

    private Long playerId;
    private String playerKey;
    private String playerNickName;
    private String playerType;
    private String playerRole;

    public ResPlayerInfoDto(Player player) {
        this.playerId = player.getPlayerId();;
        this.playerKey = player.getKey();;
        this.playerNickName = player.getNickname();
        this.playerType = player.getType();;
        this.playerRole =  player.getPlayerRole();
    }
}
