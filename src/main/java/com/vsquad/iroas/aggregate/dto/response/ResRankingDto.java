package com.vsquad.iroas.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vsquad.iroas.aggregate.entity.Player;
import com.vsquad.iroas.aggregate.entity.Ranking;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResRankingDto {

    @Schema(description = "랭킹 식별자")
    private Long rankingId;

    @Schema(description = "플레이어 닉네임")
    private String playerNickname;

    @Schema(description = "커스텀 모드 유즈맵 이름")
    private String creatorMapId;

    @Schema(description = "플레이 시간")
    private PlayTimeDto playTime;

    @Schema(description = "플레이 횟수")
    private Integer playCount;

    @Schema(description = "클리어 횟수")
    private Integer clearCount;

    public static ResRankingDto convertToDto(Ranking ranking, Player player) {

        Long rankingId = ranking.getRankingId();
        String playerNickname = player.getNickname().getPlayerNickname();
        String creatorMapId = ranking.getCreatorMapId();
        PlayTimeDto playTime = PlayTimeDto.convertToDto(ranking.getPlayTime());
        Integer playCount = ranking.getPlayCount();
        Integer clearCount = ranking.getClearCount();

        return new ResRankingDto(rankingId, playerNickname, creatorMapId, playTime, playCount, clearCount);
    }
}
