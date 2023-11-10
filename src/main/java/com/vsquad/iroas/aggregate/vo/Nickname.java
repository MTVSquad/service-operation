package com.vsquad.iroas.aggregate.vo;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Builder
@Getter
@Embeddable
public class Nickname {

    @NotNull
    @Column(name = "PLAYER_NICKNAME", length = 255)
    @Comment("닉네임")
    private String playerNickname;

    public Nickname() {}

    public Nickname(String playerNickname) {
//        validateNickname(playerNickname);
        validateByte(playerNickname);
        this.playerNickname = playerNickname;
    }

    private void validateNickname(String nickname) {

        String isRegExp = "^[가-힣a-zA-Z0-9]{2,8}$";

        Pattern pattern = Pattern.compile(isRegExp);

        Matcher matcher = pattern.matcher(nickname);

        if(!matcher.matches()) {
            throw new IllegalArgumentException("닉네임 생성 규칙과 일치하지 않습니다.");
        }
    }

    private void validateByte(String nickname) {

        int length =  nickname.getBytes().length;

        if(length >= 255) {
            throw new IllegalArgumentException("닉네임 255 byte 초과");
        }
    }
}
