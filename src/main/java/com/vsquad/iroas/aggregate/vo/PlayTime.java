package com.vsquad.iroas.aggregate.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

//@Builder
@Getter
@NoArgsConstructor
@Embeddable
public class PlayTime {

    @Column(name = "PLAY_START_TIME")
    @Comment("게임 시작 시간")
    private LocalDateTime playStartTime;

    @Column(name = "PLAY_CLEAR_TIME")
    @Comment("게임 깬 시간")
    private LocalDateTime playClearTime;

    @Column(name = "PLAY_MINUTES")
    @Comment("랭킹 기록한 플레이 총 소요 시간(분)")
    private Long playMinutes;

    public PlayTime(LocalDateTime playStartTime, LocalDateTime playClearTime) {
        if(playStartTime != null && playClearTime != null) {
            validateTime(playStartTime, playClearTime);
            calculateTime(playStartTime, playClearTime);
        }
    }

    private void validateTime(LocalDateTime playStartTime, LocalDateTime playClearTime) {

        boolean isAfter = playStartTime.isAfter(playClearTime);

        if(isAfter) {
            throw new IllegalArgumentException("클리어 시간은 플레이 시작 시간보다 빠를 수 없습니다.");
        } else {
            this.playStartTime = playStartTime;
            this.playClearTime = playClearTime;
        }
    }

    private void calculateTime(LocalDateTime playStartTime, LocalDateTime playClearTime) {

        // Duration으로 시간 차이 계산
        Duration duration = Duration.between(playStartTime, playClearTime);

        // 분 단위로 출력
        long minutes = duration.toMinutes();

        this.playMinutes = minutes;
    }
}
