package com.vsquad.iroas.aggregate.dto;

import com.vsquad.iroas.aggregate.entity.Avatar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvatarDto {

    private Long avatarId;
    private String maskColor;

    public static AvatarDto convertToDto(Avatar avatar) {
        return new AvatarDto(avatar.getAvatarId(), avatar.getMask());
    }
}
