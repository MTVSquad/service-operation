package com.vsquad.iroas.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vsquad.iroas.aggregate.vo.PlayTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayTimeDto {

    @Schema(description = "플레이 시작 시간")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime playStartTime;

    @Schema(description = "플레이 종료 시간")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime playClearTime;

    @Schema(description = "플레이 시간")
    private Long playMinutes;

    public static PlayTimeDto convertToDto(PlayTime playTime) {

        return new PlayTimeDto(playTime.getPlayStartTime(), playTime.getPlayClearTime(), playTime.getPlayMinutes());
    }
}
