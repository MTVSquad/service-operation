package com.vsquad.iroas.aggregate.entity;

import lombok.Data;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Table;

import javax.persistence.*;

@Entity
@javax.persistence.Table(name = "tb_channel")
@Table(appliesTo = "tb_channel", comment = "채널(서버 ex: 듀로탄 서버, 굴단 서버)")
@Data
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHANNEL_ID")
    @Comment("채널 식별자")
    private Integer channelId;

    @Column(name = "CHANNEL_NAME")
    @Comment("채널 이름")
    private String channelName;
}
