package com.vsquad.iroas.aggregate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResMessageDto {

    @Schema(name = "응답 메시지", example = "플레이어가 추가 되었습니다.")
    private String message;
}
