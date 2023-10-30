package com.vsquad.iroas.aggregate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqAvatarDto {

    @Schema(name = "maskColor", description = "마스크 색상", example = "red", allowableValues = {"red", "blue", "green"})
    private String maskColor;
}
