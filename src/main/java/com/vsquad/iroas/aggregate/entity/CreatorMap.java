package com.vsquad.iroas.aggregate.entity;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@javax.persistence.Table(name = "tb_channel")
@org.hibernate.annotations.Table(appliesTo = "tb_creator_map", comment = "크리에이터 툴에 의해 생성된 맵")
@Data
public class CreatorMap {

    @Id
    @Column(name = "CREATOR_MAP_ID")
    private String creatorToolId;

    @Column(name = "CREATOR_TOOL_NAME")
    private String creatorMapName;

    @Column(name = "CREATOR_TOOL_TYPE")
    private String creatorMapType;

    @Column(name = "CREATOR")
    private Long playerId;

    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;
}