package com.vsquad.iroas.aggregate.entity;

import com.vsquad.iroas.aggregate.vo.Nickname;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Table;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@javax.persistence.Table(name = "tb_player", indexes = {
        @Index(name = "IDX_PLAYER_KEY", columnList = "PLAYER_KEY", unique = true),
        @Index(name = "IDX_PLAYER_NICKNAME", columnList = "PLAYER_NICKNAME", unique = true)
}, uniqueConstraints = {
        @UniqueConstraint(name = "UK_PLAYER_KEY", columnNames = {"PLAYER_KEY"}),
        @UniqueConstraint(name = "UK_PLAYER_NICKNAME", columnNames = {"PLAYER_NICKNAME"})
})
@Table(appliesTo = "tb_player", comment = "플레이어")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLAYER_ID")
    @Comment("플레이어 식별자")
    private Long playerId;

    @NotNull
    @Column(name = "PLAYER_KEY")
    @Comment("플레이어 식별 키(스팀, 로컬)")
    private String key;

    @NotNull
    @Column(name = "PLAYER_TYPE")
    @Comment("플레이어 타입")
    private String type;

    @Embedded
    private Nickname nickname;

    @Column(name = "PLYER_AVATAR")
    @Comment("플레이어 아바타")
    private Long playerAvatar;

    @ColumnDefault("0")
    @Column(name = "PLAYER_MONEY")
    @Comment("플레이어 소지금")
    private Long playerMoney;

    @Column(name = "PLAYER_ITEMS")
    @Comment("플레이어가 소지한 아이템")
    private String playerItems;

    @ColumnDefault("'ROLE_PLAYER'")
    @Column(name = "PLAYER_ROLE")
    @Comment("플레이어 권한")
    private String playerRole;

    public Player(String key, String nickname, String type, Long playerMoney, String playerRole) {
        this.key = key;
        this.nickname = new Nickname(nickname);
        this.type = type;
        this.playerMoney = playerMoney;
        this.playerRole = playerRole;
    }

    public String getNickname() {
        return this.nickname.getPlayerNickname();
    }

    public void changeNickname(String nickname) {
        this.nickname = new Nickname(nickname);  // 내부에서 Nickname 객체 생성
    }

    public void resetPlayerStatus() {
        this.setPlayerItems(null);
        this.setPlayerMoney(0L);
        this.setPlayerAvatar(null);
    }
}
