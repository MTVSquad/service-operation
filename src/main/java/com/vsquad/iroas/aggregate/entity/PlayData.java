package com.vsquad.iroas.aggregate.entity;


import lombok.Data;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_play_data")
@org.hibernate.annotations.Table(appliesTo = "tb_play_data", comment = "플레이별 맵 데이터")
@Data
public class PlayData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLAY_DATA_ID")
    private Long playDataId;

    @Column(name = "PLAYER_ID")
    @Comment("플레이어 식별자")
    private Long playerId;

    @Column(name = "PLAY_TIME")
    @Comment("플레이 시간")
    private LocalDateTime playTime;

    @Column(name = "PLAY_COUNT")
    @Comment("플레이 횟수")
    private Integer playCount;

    @Column(name = "PLAY_CLEAR_COUNT")
    @Comment("플레이 완료 횟수")
    private Integer playClearCount;
}
