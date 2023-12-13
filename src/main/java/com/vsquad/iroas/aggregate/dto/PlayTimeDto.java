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
    private String playStartTime;

    @Schema(description = "플레이 종료 시간")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String playClearTime;

    @Schema(description = "플레이 시간")
    private Long playMilliSecond;

    @Schema(description = "소요 시간(except 시네마틱, 보상 획득 시간)")
    private Long playElapsedTime;

    public static PlayTimeDto convertToDto(PlayTime playTime) {

        String playStartTime = playTime.getPlayStartTime().toString();
        String playClearTime = playTime.getPlayClearTime().toString();
        Long playElapsedTime = playTime.getPlayElapsedTime();

        return new PlayTimeDto(playStartTime, playClearTime, playTime.getPlayMilliSecond(), playElapsedTime);
    }
}
