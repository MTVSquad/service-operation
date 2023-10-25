package com.vsquad.iroas.aggregate.entity;

import lombok.Data;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Table;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@javax.persistence.Table(name = "tb_ranking")
@Table(appliesTo = "tb_ranking", comment = "랭킹")
@Data
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RANKING_ID")
    @Comment("랭킹 식별자")
    private Long rankingId;

    @Column(name = "PLAYER_ID")
    @Comment("플레이어")
    private Long playId;

    @Column(name = "USEMAP")
    @Comment("커스텀 모드 유즈맵")
    private Long useMap;

    @Column(name = "CLEAR_TIME")
    @Comment("게임 깬 시간")
    private LocalTime clearTime;
}
