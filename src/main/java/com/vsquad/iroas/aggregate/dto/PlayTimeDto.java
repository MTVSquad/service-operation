package com.vsquad.iroas.aggregate.dto;

import com.vsquad.iroas.aggregate.vo.PlayTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayTimeDto {

    private LocalDateTime playStartTime;

    private LocalDateTime playClearTime;

    private Long playMinutes;

    public static PlayTimeDto convertToDto(PlayTime playTime) {

        return new PlayTimeDto(playTime.getPlayStartTime(), playTime.getPlayClearTime(), playTime.getPlayMinutes());
    }
}
