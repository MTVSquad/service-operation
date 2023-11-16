package com.vsquad.iroas.aggregate.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Table(name = "tb_play_data")
@org.hibernate.annotations.Table(appliesTo = "tb_play_data", comment = "플레이별 맵 데이터")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlayData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLAY_DATA_ID")
    private Long playDataId;

    @Column(name = "PLAYER_ID")
    @Comment("플레이어 식별자")
    private Long playerId;

    @Column(name = "CREATOR_MAP_ID")
    @Comment("크리에이터 맵 식별자")
    private String creatorMapId;

    @Column(name = "PLAY_TIME")
    @Comment("플레이 시간")
    private Long playTime;

    @Column(name = "PLAY_COUNT")
    @Comment("플레이 횟수")
    private Integer playCount;

    @Column(name = "PLAY_CLEAR_COUNT")
    @Comment("플레이 완료 횟수")
    private Integer playClearCount;

    public PlayData(Long playerId, String creatorMapId, Long playTime, Integer playCount, Integer playClearCount) {
        this.playerId = playerId;
        this.creatorMapId = creatorMapId;
        this.playTime = playTime;
        this.playCount = playCount;
        this.playClearCount = playClearCount;
    }
}
