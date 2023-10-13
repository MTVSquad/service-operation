package com.vsquad.iroas.aggregate.entity;

import lombok.Data;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Table;

import javax.persistence.*;

@Entity
@javax.persistence.Table(name = "tb_item")
@Table(appliesTo = "tb_item", comment = "아이템")
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID")
    @Comment("아이템 식별자")
    private Integer itemId;

    @Column(name = "ITEM_NAME")
    @Comment("아이템 이름")
    private String itemName;

    @Column(name = "ITEM_TYPE")
    @Comment("아이템 종류 ex : 칼, 총, etc")
    private String itemType;

    @Column(name = "ITEM_DAMAGE")
    @Comment("아이템 데미지")
    private Integer damage;
}
