package com.vsquad.iroas.aggregate.entity;

import lombok.Data;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Table;

import javax.persistence.*;

@Entity
@Table(appliesTo = "TB_AVATAR", comment = "플레이어 아바타")
@Data
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AVATAR_ID")
    @Comment("아바타 식별자")
    private Long avatarId;

    @Column(name = "AVATAR_MASK")
    @Comment("아바타 마스크")
    private String mask;
}
