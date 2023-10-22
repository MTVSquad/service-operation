package com.vsquad.iroas.aggregate.entity;

import lombok.Data;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Table;

import javax.persistence.*;

@Entity
@javax.persistence.Table(name = "tb_enemy_spawner")
@Table(appliesTo = "tb_enemy_spawner", comment = "에너미 스포너(소환)")
@Data
public class EnemySpawner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ENEMY_SPAWNER_ID")
    @Comment("에너미 스포너 식별자")
    private Integer enemySpawnerId;

    @Column(name = "ENEMY_SPAWNER_NAME")
    @Comment("에너미 스포너 이름")
    private String enemySpawnerName;

    @Column(name = "ENEMY_SPAWNER_TYPE")
    @Comment("에너미 스포너 종류(근접, 원거리)")
    private String enemySpawnerType;

    @Column(name = "ENEMY_SPAWNER_GRADE")
    @Comment("에너미 스포너 소환 종류(보스, 엘리트, 일반몹")
    private String enemySpawnerGrade;

    @Column(name = "CREATOR_MAP_ID")
    @Comment("크리에이터 툴에 의해 생성된 맵 식별자")
    private String creatorMapId;
}
