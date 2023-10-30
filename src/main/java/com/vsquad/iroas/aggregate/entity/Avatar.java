package com.vsquad.iroas.aggregate.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Table;

import javax.persistence.*;

@Entity
@javax.persistence.Table(name = "tb_avatar", uniqueConstraints = {
        @UniqueConstraint(name = "UK_AVATAR_PLAYER_ID", columnNames = {"PLAYER_ID"})
}, indexes = {
        @Index(name = "IDX_AVATAR_PLAYER_ID", columnList = "PLAYER_ID", unique = true)
})
@Table(appliesTo = "tb_avatar", comment = "플레이어 아바타")
@NoArgsConstructor
@Data
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AVATAR_ID")
    @Comment("아바타 식별자")
    private Long avatarId;

    @Column(name = "AVATAR_MASK")
    @Comment("아바타 마스크 컬러")
    private String mask;

    @Column(name = "PLAYER_ID")
    @Comment("플레이어")
    private Long playerId;

    public Avatar(Long playerId, String playerMaskColor) {
        this.playerId = playerId;
        this.mask = playerMaskColor;
    }
}
