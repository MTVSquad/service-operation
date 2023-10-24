package com.vsquad.iroas.aggregate.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsquad.iroas.aggregate.entity.EnemySpawner;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EnemySpawnerDto {

    private String enemySpawnerName;

    private List<Float> enemySpawnerStartPoint;

    private Integer enemySpawnerAmount;

    private Float enemySpawnerStartDelay;

    private Float enemySpawnerInterval;

    private EnemyDto enemy;

    public static EnemySpawner convertToEntity(EnemySpawnerDto enemySpawnerDto) throws JsonProcessingException {

        if(enemySpawnerDto == null) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String startPoint = objectMapper.writeValueAsString(enemySpawnerDto.getEnemySpawnerStartPoint());

        EnemySpawner enemySpawner = new EnemySpawner();
        enemySpawner.setEnemySpawnerName(enemySpawnerDto.getEnemySpawnerName());
        enemySpawner.setSpawnerAmount(enemySpawnerDto.getEnemySpawnerAmount());
        enemySpawner.setSpawnerInterval(enemySpawnerDto.getEnemySpawnerInterval());
        enemySpawner.setSpawnerStartDelay(enemySpawnerDto.getEnemySpawnerStartDelay());
        enemySpawner.setSpawnerStartPoint(startPoint);
        enemySpawner.setEnemy(EnemyDto.convertToEntity(enemySpawnerDto.getEnemy()));

        return enemySpawner;
    }
}
