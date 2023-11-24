package com.vsquad.iroas.aggregate.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vsquad.iroas.aggregate.dto.CustomLocalDateTimeDeserializer;
import com.vsquad.iroas.aggregate.entity.CreatorMap;
import com.vsquad.iroas.aggregate.entity.EnemySpawner;
import com.vsquad.iroas.aggregate.entity.Prop;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResCreatorMapDto {

    @Schema(name = "creatorMapId", description = "맵 식별자")
    private Long creatorMapId;

    @Schema(name = "creatorMapName", description = "맵 이름은 2~12자 한글, 영문 대소문자, 숫자만 가능")
//    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,12}$")
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
    private List<ResEnemySpawnerDto> enemySpawnerList;

    @Schema(name = "propList", description = "구조물 목록")
    private List<ResPropDto> propList;

    public ResCreatorMapDto(String creatorMapType, LocalDateTime createTime, Double playerStartPointXLocation, Double playerStartPointYLocation, Double playerStartPointZLocation, Double playerStartPointYaw, String timezone, List<ResEnemySpawnerDto> enemySpawnerList, List<ResPropDto> propList) {
        this.creatorMapType = creatorMapType;
        this.createTime = createTime;
        this.playerStartPointXLocation = playerStartPointXLocation;
        this.playerStartPointYLocation = playerStartPointYLocation;
        this.playerStartPointZLocation = playerStartPointZLocation;
        this.playerStartPointYaw = playerStartPointYaw;
        this.timezone = timezone;
        this.enemySpawnerList = enemySpawnerList;
        this.propList = propList;
    }

    public CreatorMap convertToEntity(ResCreatorMapDto resCreatorMapDto) throws JsonProcessingException {

        if(resCreatorMapDto == null) {
            return null;
        }

        List<ResEnemySpawnerDto> enemySpawnerDtos =  resCreatorMapDto.getEnemySpawnerList();
        List<EnemySpawner> enemySpawners = new ArrayList<>();

        for (ResEnemySpawnerDto enemySpawnerDto : enemySpawnerDtos) {
            enemySpawners.add(enemySpawnerDto.convertToEntity(enemySpawnerDto));
        }

        List<ResPropDto> propDtos = resCreatorMapDto.getPropList();
        List<Prop> props = new ArrayList<>();

        for (ResPropDto propDto : propDtos) {
            props.add(propDto.convertToEntity(propDto));
        }

        CreatorMap creatorMap = new CreatorMap();
//        creatorMap.setCreatorMapId(creatorMapDto.getCreatorMapId());
//        creatorMap.setCreatorMapName(creatorMapDto.getCreatorMapName());
        creatorMap.setCreatorMapType(resCreatorMapDto.getCreatorMapType());
//        creatorMap.setCreator(creatorMapDto.getCreator());
        creatorMap.setCreateTime(resCreatorMapDto.getCreateTime());
        creatorMap.setPlayerStartPointXLocation(resCreatorMapDto.getPlayerStartPointXLocation());
        creatorMap.setPlayerStartPointYLocation(resCreatorMapDto.getPlayerStartPointYLocation());
        creatorMap.setPlayerStartPointZLocation(resCreatorMapDto.getPlayerStartPointZLocation());
        creatorMap.setPlayerStartPointYaw(resCreatorMapDto.getPlayerStartPointYaw());
        creatorMap.setTimezone(resCreatorMapDto.getTimezone());
        creatorMap.setEnemySpawnerList(enemySpawners);
        creatorMap.setPropList(props);

        return creatorMap;
    }
}