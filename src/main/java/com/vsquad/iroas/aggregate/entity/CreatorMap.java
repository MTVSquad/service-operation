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

    @Column(name = "PLAYER_START_POINT_X_LOCATION")
    @Comment("플레이어 시작 지점 x 좌표")
    private Double playerStartPointXLocation;

    @Column(name = "PLAYER_START_POINT_Y_LOCATION")
    @Comment("플레이어 시작 지점 y 좌표")
    private Double playerStartPointYLocation;

    @Column(name = "PLAYER_START_POINT_Z_LOCATION")
    @Comment("플레이어 시작 지점 z 좌표")
    private Double playerStartPointZLocation;

    @Column(name = "PLAYER_START_POINT_YAW")
    @Comment("플레이어 시작 지점 회전 값")
    private Double playerStartPointYaw;

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
}