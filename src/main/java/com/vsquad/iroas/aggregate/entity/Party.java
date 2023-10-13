package com.vsquad.iroas.aggregate.entity;

import lombok.Data;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Table;

import javax.persistence.*;

@Entity
@javax.persistence.Table(name = "tb_party")
@Table(appliesTo = "tb_party", comment = "멀티 플레이 시 파티 단위")
@Data
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PARTY_ID")
    @Comment("파티 식별자")
    private Long partyId;

    @Column(name = "CHANNEL_ID")
    @Comment("채널")
    private Long channelId;

    @Column(name = "PARTY_HOST")
    @Comment("파티 호스트(방장)")
    private Long partyHost;

    @Column(name = "PARTY_GUEST")
    @Comment("파티 게스트(파티 참여자)")
    private Long partyGuest;
}
