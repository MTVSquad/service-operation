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
import java.util.List;


@Entity
@javax.persistence.Table(name = "tb_player")
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
    @Column(name = "PLAYER_STEAM_KEY")
    @Comment("플레이어 스팀 식별 키")
    private String playerSteamKey;

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

    public Player(String steamKey, String nickname) {
        this.playerSteamKey = steamKey;
        this.nickname = new Nickname(nickname);
    }
}
