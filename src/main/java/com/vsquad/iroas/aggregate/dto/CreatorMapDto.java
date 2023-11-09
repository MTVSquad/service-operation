package com.vsquad.iroas.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vsquad.iroas.aggregate.entity.CreatorMap;
import com.vsquad.iroas.aggregate.entity.EnemySpawner;
import com.vsquad.iroas.aggregate.entity.Prop;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatorMapDto {

    @Schema(name = "creatorMapId", description = "맵 식별자")
    private String creatorMapId;

    @Schema(name = "creatorMapName", description = "맵 이름은 2~12자 한글, 영문 대소문자, 숫자만 가능")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,12}$")
    private String creatorMapName;

    @Schema(name = "creatorMapType", description = "맵 타입", allowableValues = {"MELEE", "TIME_ATTACK"})
    private String creatorMapType;

    @Schema(name = "creator", description = "맵 생성자")
    private String creator;

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

    public CreatorMap convertToEntity(CreatorMapDto creatorMapDto) throws JsonProcessingException {

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
//        creatorMap.setCreatorMapId(creatorMapDto.getCreatorMapId());
//        creatorMap.setCreatorMapName(creatorMapDto.getCreatorMapName());
        creatorMap.setCreatorMapType(creatorMapDto.getCreatorMapType());
//        creatorMap.setCreator(creatorMapDto.getCreator());
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