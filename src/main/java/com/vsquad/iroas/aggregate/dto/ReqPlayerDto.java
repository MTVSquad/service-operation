package com.vsquad.iroas.aggregate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class ReqPlayerDto {

    @Schema(name = "steamKey", description = "스팀 플레이어 식별정보", example = "12345678901234567")
    private String steamKey;

    @Schema(name = "playerNickName", description = "닉네임은 2~8자 한글, 영문 대소문자, 숫자만 가능", example = "히에로스")
    @NonNull
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,8}$")
    private String playerNickName;
}
