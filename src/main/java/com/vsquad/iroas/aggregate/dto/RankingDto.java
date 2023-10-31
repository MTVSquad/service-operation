package com.vsquad.iroas.aggregate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankingDto {

    @Schema(description = "랭킹 식별자")
    private Long rankingId;

    @Schema(description = "플레이어 식별자")
    private Long playerId;

    @Schema(description = "커스텀 모드 유즈맵 식별자")
    private String creatorMapId;

    @Schema(description = "플레이 시간")
    private PlayTimeDto playTime;

    @Schema(description = "플레이 횟수")
    private Integer playCount;

    @Schema(description = "클리어 횟수")
    private Integer clearCount;
}
