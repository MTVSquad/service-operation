package com.vsquad.iroas.aggregate.dto;

import com.vsquad.iroas.aggregate.entity.Enemy;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnemyDto {

    @Schema(name = "enemyCode", description = "에네미 코드")
    private String enemyCode;

    @Schema(name = "enemyName", description = "에네미 이름", example = "enemy1")
    private String enemyName;

    @Schema(name = "enemyType", description = "에네미 타입(근거리, 원거리, 엘리트)", allowableValues = {"Melee", "Ranged_Ground", "Ranged_Air", "Elite"})
    private String enemyType;

    @Schema(name = "enemyHp", description = "에네미 체력", example = "100")
    private Long enemyHp;

    @Schema(name = "enemyPower", description = "에네미 공격력", example = "10")
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
