package com.vsquad.iroas.aggregate.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vsquad.iroas.aggregate.dto.CustomLocalDateTimeDeserializer;
import com.vsquad.iroas.aggregate.dto.EnemySpawnerDto;
import com.vsquad.iroas.aggregate.dto.PropDto;
import com.vsquad.iroas.aggregate.entity.CreatorMap;
import com.vsquad.iroas.aggregate.entity.EnemySpawner;
import com.vsquad.iroas.aggregate.entity.Prop;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqCreatorMapDto {

    @Schema(name = "creatorMapType", description = "맵 타입", allowableValues = {"MELEE", "TIME_ATTACK"})
    private String creatorMapType;

    @Schema(name = "createTime", description = "맵 생성 시간", example = "2023-10-25 06:25:56", defaultValue = "2023-10-25 06:25:56")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @Schema(name = "playerStartPointXLocation", description = "플레이어 시작 지점 x 좌표")
    private Double playerStartPointXLocation;

    @Schema(name = "playerStartPointYLocation", description = "플레이어 시작 지점 y 좌표")
    private Double playerStartPointYLocation;

    @Schema(name = "playerStartPointZLocation", description = "플레이어 시작 지점 z 좌표")
    private Double playerStartPointZLocation;

    @Schema(name = "playerStartPointYaw", description = "플레이어 시작 지점 회전 값")
    private Double playerStartPointYaw;

    @Schema(name = "timezone", description = "맵 타임존(아침/낮/밤)", allowableValues = {"Morning", "Noon", "Evening"})
    private String timezone;

    @Schema(name = "enemySpawnerList", description = "에네미 스포너 목록")
    private List<EnemySpawnerDto> enemySpawnerList;

    @Schema(name = "propList", description = "구조물 목록")
    private List<PropDto> propList;

    @Schema(name = "creator", description = "제작자")
    private Long creator;

    public CreatorMap convertToEntity(ReqCreatorMapDto creatorMapDto) {

        if(creatorMapDto == null) {
            return null;
        }

        List<EnemySpawnerDto> enemySpawnerDtos =  creatorMapDto.getEnemySpawnerList();
        List<EnemySpawner> enemySpawners = new ArrayList<>();

        for (EnemySpawnerDto enemySpawnerDto : enemySpawnerDtos) {
            enemySpawners.add(EnemySpawnerDto.convertToEntity(enemySpawnerDto));
        }

        List<PropDto> propDtos = creatorMapDto.getPropList();
        List<Prop> props = new ArrayList<>();

        for (PropDto propDto : propDtos) {
            props.add(PropDto.convertToEntity(propDto));
        }

        CreatorMap creatorMap = new CreatorMap();
        creatorMap.setCreatorMapType(creatorMapDto.getCreatorMapType());
        creatorMap.setCreateTime(creatorMapDto.getCreateTime());
        creatorMap.setPlayerStartPointXLocation(creatorMapDto.getPlayerStartPointXLocation());
        creatorMap.setPlayerStartPointYLocation(creatorMapDto.getPlayerStartPointYLocation());
        creatorMap.setPlayerStartPointZLocation(creatorMapDto.getPlayerStartPointZLocation());
        creatorMap.setPlayerStartPointYaw(creatorMapDto.getPlayerStartPointYaw());
        creatorMap.setTimezone(creatorMapDto.getTimezone());
        creatorMap.setEnemySpawnerList(enemySpawners);
        creatorMap.setPropList(props);

        return creatorMap;
    }
}
