package com.vsquad.iroas.aggregate.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vsquad.iroas.aggregate.dto.CustomLocalDateTimeDeserializer;
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
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime playStartTime;

    @Column(name = "PLAY_CLEAR_TIME")
    @Comment("게임 깬 시간")
    private LocalDateTime playClearTime;

    @Column(name = "PLAY_MILLI_SECOND")
    @Comment("랭킹 기록한 플레이 총 소요 시간")
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private Long playMilliSecond;

    @Column(name = "PLAY_ELAPSED_TIME")
    @Comment("랭킹 기록한 플레이 총 소요 시간")
    private Long playElapsedTime;

    public PlayTime(LocalDateTime playStartTime, LocalDateTime playClearTime, Long playElapsedTime) {
        if(playStartTime != null && playClearTime != null) {
            validateTime(playStartTime, playClearTime, playElapsedTime);
            calculateTime(playStartTime, playClearTime);
        }
    }

    private void validateTime(LocalDateTime playStartTime, LocalDateTime playClearTime, Long playElapsedTime) {

        boolean isAfter = playStartTime.isAfter(playClearTime);

        if(isAfter) {
            throw new IllegalArgumentException("클리어 시간은 플레이 시작 시간 보다 빠를 수 없음");
        } else {
            this.playStartTime = playStartTime;
            this.playClearTime = playClearTime;
            this.playElapsedTime = playElapsedTime;
        }
    }

    private void calculateTime(LocalDateTime playStartTime, LocalDateTime playClearTime) {

        // Duration으로 시간 차이 계산
        Duration duration = Duration.between(playStartTime, playClearTime);

        // 분, 초 단위로 출력
//        long minutes = duration.toMinutes();
//        long seconds = duration.getSeconds() % 60;

        long milliSec = duration.toMillis();

        this.playMilliSecond = milliSec;
    }
}
