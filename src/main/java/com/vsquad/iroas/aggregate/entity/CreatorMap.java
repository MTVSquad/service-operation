package com.vsquad.iroas.aggregate.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsquad.iroas.aggregate.dto.CreatorMapDto;
import com.vsquad.iroas.aggregate.dto.EnemySpawnerDto;
import com.vsquad.iroas.aggregate.dto.PropDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_creator_map")
@org.hibernate.annotations.Table(appliesTo = "tb_creator_map", comment = "크리에이터 툴에 의해 생성된 맵")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatorMap {

    @Id
    @Column(name = "CREATOR_MAP_ID")
    @Comment("크리에이터 툴에 의해 생성된 맵 식별자")
    private String creatorMapId;

    @Column(name = "CREATOR_TOOL_NAME")
    @Comment("맵 이름")
    private String creatorMapName;

    @Column(name = "CREATOR_TOOL_TYPE", columnDefinition = "ENUM('MELEE', 'TIME_ATTACK')")
    @Comment("맵 타입")
    private String creatorMapType;

    @Column(name = "CREATOR")
    @Comment("맵 생성자")
    private Long creator;

    @Column(name = "CREATE_TIME")
    @Comment("맵 생성 시간")
    private LocalDateTime createTime;

    @Column(name = "PLAYER_START_POINT")
    @Comment("플레이어 시작 지점 [x, y, z, yaw]")
    private String playerStartPoint;

    @Column(name = "TIMEZONE", columnDefinition = "ENUM('Morning', 'Noon', 'Evening')")
    @Comment("맵 타임존(낮/밤)")
    private String timezone;

    @JoinColumn(name = "CREATOR_MAP_ID")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Comment("에네미 스포너 목록")
    private List<EnemySpawner> enemySpawnerList;

    @JoinColumn(name = "CREATOR_MAP_ID")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Comment("구조물 목록")
    private List<Prop> propList;

    public static CreatorMapDto convertToDto(CreatorMap map) throws JsonProcessingException {

        if(map == null) return null;

        ObjectMapper objectMapper = new ObjectMapper();

        List<Double> startPoint = objectMapper.readValue(map.getPlayerStartPoint(), List.class);
        try {
            List<EnemySpawnerDto>  enemySpawnerDtos = objectMapper.readValue((JsonParser) map.getEnemySpawnerList(), List.class);

            List<PropDto> propDtos = objectMapper.readValue((JsonParser) map.getPropList(), List.class);

            return new CreatorMapDto(map.getCreatorMapId(), map.getCreatorMapName(), map.getCreatorMapType(), map.getCreator(),
                map.getCreateTime(), startPoint, map.getTimezone(), enemySpawnerDtos, propDtos);

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            System.err.println("Error: " + errorMessage);
            e.printStackTrace();
        }

        return null;
    }
}