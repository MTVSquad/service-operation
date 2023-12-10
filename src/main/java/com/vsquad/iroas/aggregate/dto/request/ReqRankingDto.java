package com.vsquad.iroas.aggregate.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vsquad.iroas.aggregate.dto.CustomLocalDateTimeDeserializer;
import com.vsquad.iroas.aggregate.entity.Ranking;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReqRankingDto {

    @Schema(description = "커스텀 모드 유즈맵")
    private Long creatorMapId;

    @Schema(description = "게임 시작 시간", example = "2023-12-06 00:00:00", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime playStartTime;

    @Schema(description = "게임 깬 시간", example = "2023-12-06 00:05:00", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime playClearTime;

    @Schema(description = "클리어 여부")
    private Boolean clearYn;

    @Schema(description = "게임 소요 시간", example = "280000")
    private Long elapsedTime;
}
