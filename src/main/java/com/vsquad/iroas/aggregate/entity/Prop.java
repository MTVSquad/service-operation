package com.vsquad.iroas.aggregate.entity;

import lombok.Data;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Table(name = "tb_prop")
@org.hibernate.annotations.Table(appliesTo = "tb_prop", comment = "플레이어와 상호작용 가능한 오브젝트")
@Data
public class Prop {

    @Id
    @Column(name = "PROP_ID")
    @Comment("PROP 식별자")
    private String propId;

    @Column(name = "PROP_NAME")
    @Comment("PROP 이름")
    private String propName;

    @Column(name = "PROP_LOCATION")
    @Comment("PROP 위치 값")
    private String propLocation;

    @Column(name = "CREATOR_MAP_ID")
    @Comment("크리에이터 툴에 의해 생성된 맵 식별자")
    private String creatorMapId;
}
