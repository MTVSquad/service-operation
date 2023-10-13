package com.vsquad.iroas.aggregate.entity;

import lombok.Data;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Table;

import javax.persistence.*;

@Entity
@javax.persistence.Table(name = "tb_enemy")
@Table(appliesTo = "tb_enemy", comment = "에너미(몬스터)")
@Data
public class Enemy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ENEMY_ID")
    @Comment("에너미 식별자")
    private Long enemyId;

    @Column(name = "ENEMY_NAME")
    @Comment("에너미 이름")
    private String enemyName;

    @Column(name = "ENEMY_TYPE")
    @Comment("에너미 종류(근접, 원거리)")
    private String enemyType;

    @Column(name = "ENEMY_GRADE")
    @Comment("에너미 등급(보스, 엘리트, 일반몹)")
    private String enemyGrade;

    @Column(name = "ENEMY_HP")
    @Comment("에너미 체력")
    private Long enemyHp;

    @Column(name = "ENEMY_SKILL")
    @Comment("에너미 특수스킬")
    private String enemySkill;

    @Column(name = "ENEMY_DAMAGE")
    @Comment("에너미 공격력")
    private Long enemyDamage;
}
