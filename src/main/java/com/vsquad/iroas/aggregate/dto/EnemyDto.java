package com.vsquad.iroas.aggregate.dto;

import com.vsquad.iroas.aggregate.entity.Enemy;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EnemyDto {

    private String enemyCode;

    private String enemyName;

    private String enemyType;

    private Long enemyHp;

    private Long enemyPower;

    public static Enemy convertToEntity(EnemyDto enemyDto) {

        if(enemyDto == null) {
            return null;
        }

        Enemy enemy = new Enemy();
        enemy.setEnemyCode(enemyDto.getEnemyCode());
        enemy.setEnemyName(enemyDto.getEnemyName());
        enemy.setEnemyType(enemyDto.getEnemyType());
        enemy.setEnemyHp(enemyDto.getEnemyHp());
        enemy.setEnemyPower(enemyDto.getEnemyPower());

        return enemy;
    }
}
