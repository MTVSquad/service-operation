package com.vsquad.iroas.aggregate.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Table;

import javax.persistence.*;

@Entity
@javax.persistence.Table(name = "tb_enemy")
@Table(appliesTo = "tb_enemy", comment = "에너미(몬스터)")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enemy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ENEMY_ID")
    @Comment("에너미 식별자")
    private Long enemyId;

    @Column(name = "ENEMY_CODE")
    @Comment("클라이언트에서 보내준 에네미 식별 코드")
    private String enemyCode;

    @Column(name = "ENEMY_NAME")
    @Comment("에너미 이름")
    private String enemyName;

    @Column(name = "ENEMY_TYPE", columnDefinition = "ENUM('Melee', 'Ranged_Ground', 'Ranged_Air', 'Elite')")
    @Comment("에너미 종류(근접, 원거리)")
    private String enemyType;

    @Column(name = "ENEMY_HP")
    @Comment("에너미 체력")
    private Long enemyHp;

    @Column(name = "ENEMY_POWER")
    @Comment("에너미 공격력")
    private Long enemyPower;

    @Column(name = "ENEMY_SPAWNER")
    @Comment("에너미 스포너")
    private Integer enemySpawnerId;

    @Column(name = "CREATOR_MAP")
    @Comment("크리에이터 맵")
    private String creatorMap;

    public Enemy(String enemyCode, String enemyName, String enemyType, Long enemyHp, String enemySkill, Long enemyPower, Integer enemySpawnerId, String creatorMap) {
        this.enemyCode = enemyCode;
        this.enemyName = enemyName;
        this.enemyType = enemyType;
        this.enemyHp = enemyHp;
        this.enemyPower = enemyPower;
        this.enemySpawnerId = enemySpawnerId;
        this.creatorMap = creatorMap;
    }
}
