package com.vsquad.iroas.aggregate.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vsquad.iroas.aggregate.entity.CreatorMap;
import com.vsquad.iroas.aggregate.entity.Enemy;
import com.vsquad.iroas.aggregate.entity.EnemySpawner;
import com.vsquad.iroas.aggregate.entity.Prop;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqCreatorMapDto {

    private String creatorMapId;

    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,12}$")
    private String creatorMapName;

    private String creatorMapType;

    private Long creator;

    private LocalDateTime createTime;

    private String playerStartPoint;

    private String timezone;

    private List<EnemySpawnerDto> enemySpawnerList;

    private List<PropDto> propList;

    public CreatorMap convertToEntity(ReqCreatorMapDto creatorMapDto) throws JsonProcessingException {

        if(creatorMapDto == null) {
            return null;
        }

        List<EnemySpawnerDto> enemySpawnerDtos =  creatorMapDto.getEnemySpawnerList();
        List<EnemySpawner> enemySpawners = new ArrayList<>();

        for (EnemySpawnerDto enemySpawnerDto : enemySpawnerDtos) {
            enemySpawners.add(enemySpawnerDto.convertToEntity(enemySpawnerDto));
        }

        List<PropDto> propDtos = creatorMapDto.getPropList();
        List<Prop> props = new ArrayList<>();

        for (PropDto propDto : propDtos) {
            props.add(propDto.convertToEntity(propDto));
        }

        CreatorMap creatorMap = new CreatorMap();
        creatorMap.setCreatorMapId(creatorMapDto.getCreatorMapId());
        creatorMap.setCreatorMapName(creatorMapDto.getCreatorMapName());
        creatorMap.setCreatorMapType(creatorMapDto.getCreatorMapType());
        creatorMap.setCreator(creatorMapDto.getCreator());
        creatorMap.setCreateTime(creatorMapDto.getCreateTime());
        creatorMap.setPlayerStartPoint(creatorMapDto.getPlayerStartPoint());
        creatorMap.setTimezone(creatorMapDto.getTimezone());
        creatorMap.setEnemySpawnerList(enemySpawners);
        creatorMap.setPropList(props);

        return creatorMap;
    }
}
