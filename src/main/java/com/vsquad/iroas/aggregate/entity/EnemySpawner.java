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
    private Long enemySpawnerId;

    @Column(name = "CREATOR_MAP_ID")
    @Comment("크리에이터 툴에 의해 생성된 맵 식별자")
    private String creatorMapId;

    @Column(name = "ENEMY_START_POINT_X_LOCATION")
    @Comment("에네미 시작 지점 x 좌표")
    private Double enemyStartPointXLocation;

    @Column(name = "ENEMY_START_POINT_Y_LOCATION")
    @Comment("에네미 시작 지점 y 좌표")
    private Double enemyStartPointYLocation;

    @Column(name = "ENEMY_START_POINT_Z_LOCATION")
    @Comment("에네미 시작 지점 z 좌표")
    private Double enemyStartPointZLocation;

    @Column(name = "SPAWNER_AMOUNT")
    @Comment("에너미 스포너 소환 수")
    private Integer enemySpawnAmount;

    @Column(name = "SPAWNER_START_DELAY")
    @Comment("에너미 스포너 시작 딜레이")
    private Double enemySpawnerStartDelay;

    @Column(name = "SPAWNER_INTERVAL")
    @Comment("에너미 스포너 소환 간격")
    private Double enemySpawnerInterval;

    @Column(name = "ENEMY_TYPE", columnDefinition = "ENUM('Melee', 'Ranged_Ground', 'Ranged_Air', 'Elite')")
    @Comment("에너미 종류(근접, 원거리)")
    private String enemyType;

    @Column(name = "ENEMY_HP")
    @Comment("에너미 체력")
    private Long enemyHp;

    @Column(name = "ENEMY_POWER")
    @Comment("에너미 공격력")
    private Long enemyPower;
}
