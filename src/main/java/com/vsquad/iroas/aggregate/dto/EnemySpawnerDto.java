package com.vsquad.iroas.aggregate.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vsquad.iroas.aggregate.entity.EnemySpawner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnemySpawnerDto {

    @Schema(name = "enemyStartPointXLocation", description = "에네미 시작 지점 x 좌표", example = "10.00")
    private Double enemyStartPointXLocation;

    @Schema(name = "enemyStartPointYLocation", description = "에네미 시작 지점 y 좌표", example = "10.00")
    private Double enemyStartPointYLocation;

    @Schema(name = "enemyStartPointZLocation", description = "에네미 시작 지점 z 좌표", example = "10.00")
    private Double enemyStartPointZLocation;

    @Schema(name = "enemySpawnAmount", description = "에네미 스포너 에너미 생성 수", example = "10")
    private Integer enemySpawnAmount;

    @Schema(name = "enemySpawnerStartDelay", description = "에네미 스포너 시작 딜레이", example = "10.00")
    private Double enemySpawnerStartDelay;

    @Schema(name = "enemySpawnerInterval", description = "에네미 스포너 생성 간격", example = "10.00")
    private Double enemySpawnerInterval;

    @Schema(name = "enemyType", description = "에네미 타입(근거리, 원거리, 엘리트)", allowableValues = {"Melee", "Ranged_Ground", "Ranged_Air", "Elite"})
    private String enemyType;

    @Schema(name = "enemyHp", description = "에네미 체력", example = "100")
    private Long enemyHp;

    @Schema(name = "enemyPower", description = "에네미 공격력", example = "10")
    private Long enemyPower;

    public static EnemySpawner convertToEntity(EnemySpawnerDto enemySpawnerDto) throws JsonProcessingException {

        if(enemySpawnerDto == null) {
            return null;
        }

        EnemySpawner enemySpawner = new EnemySpawner();
        enemySpawner.setSpawnerAmount(enemySpawnerDto.getEnemySpawnAmount());
        enemySpawner.setSpawnerInterval(enemySpawnerDto.getEnemySpawnerInterval());
        enemySpawner.setSpawnerStartDelay(enemySpawnerDto.getEnemySpawnerStartDelay());
        enemySpawner.setEnemyStartPointXLocation(enemySpawnerDto.getEnemyStartPointXLocation());
        enemySpawner.setEnemyStartPointYLocation(enemySpawnerDto.getEnemyStartPointYLocation());
        enemySpawner.setEnemyStartPointZLocation(enemySpawnerDto.getEnemyStartPointZLocation());
        enemySpawner.setEnemyType(enemySpawnerDto.getEnemyType());
        enemySpawner.setEnemyHp(enemySpawnerDto.getEnemyHp());
        enemySpawner.setEnemyPower(enemySpawnerDto.getEnemyPower());

        return enemySpawner;
    }
}
