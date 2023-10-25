package com.vsquad.iroas.aggregate.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsquad.iroas.aggregate.entity.EnemySpawner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnemySpawnerDto {

    @Schema(name = "enemySpawnerName", description = "에네미 스포너 이름", example = "enemySpawner1")
    private String enemySpawnerName;

    @Schema(name = "enemySpawnerStartPoint", description = "에네미 스포너 시작 지점 [x, y, z, yaw]")
    private List<Float> enemySpawnerStartPoint;

    @Schema(name = "enemySpawnerAmount", description = "에네미 스포너 생성 수", example = "10")
    private Integer enemySpawnerAmount;

    @Schema(name = "enemySpawnerStartDelay", description = "에네미 스포너 시작 딜레이", example = "10.00")
    private Float enemySpawnerStartDelay;

    @Schema(name = "enemySpawnerInterval", description = "에네미 스포너 생성 간격", example = "10.00")
    private Float enemySpawnerInterval;

    @Schema(name = "enemy", description = "에네미 정보")
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
