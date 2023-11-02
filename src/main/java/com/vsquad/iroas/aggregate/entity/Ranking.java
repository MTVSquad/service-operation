package com.vsquad.iroas.aggregate.entity;

import com.vsquad.iroas.aggregate.vo.PlayTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Table;

import javax.persistence.*;

@Entity
@javax.persistence.Table(name = "tb_ranking", uniqueConstraints = {
        @UniqueConstraint(name = "UK_RANKING", columnNames = {"PLAYER_ID", "CREATOR_MAP_ID"})
})
@Table(appliesTo = "tb_ranking", comment = "랭킹")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RANKING_ID")
    @Comment("랭킹 식별자")
    private Long rankingId;

    @Column(name = "PLAYER_ID")
    @Comment("플레이어")
    private Long playerId;

    @Column(name = "CREATOR_MAP_ID")
    @Comment("커스텀 모드 유즈맵")
    private String creatorMapId;

    @Embedded
    private PlayTime playTime;

    @Column(name = "PLAY_COUNT")
    @Comment("플레이 횟수")
    private Integer playCount;

    @Column(name = "CLEAR_COUNT")
    @Comment("클리어 횟수")
    private Integer clearCount;

    public Ranking(Long playerId, String creatorMapId, PlayTime playTime, Integer playCount, Integer clearCount) {
        this.playerId = playerId;
        this.creatorMapId = creatorMapId;
        this.playTime = playTime;
        this.playCount = playCount;
        this.clearCount = clearCount;
    }
}
