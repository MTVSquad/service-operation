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

    @Schema(name = "스팀에서 받아온 회원 식별 정보")
    private String steamKey;

    @Schema(name = "플레이어 닉네임")
    @NonNull
    @Pattern(regexp = "^[가-힣a-zA-Z]{2,8}$")
    private String playerNickName;
}
