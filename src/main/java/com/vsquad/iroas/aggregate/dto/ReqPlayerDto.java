package com.vsquad.iroas.aggregate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class ReqPlayerDto {

    @Pattern(regexp = "^[가-힣a-zA-Z]{2,8}$")
    private String playerNickName;
}
