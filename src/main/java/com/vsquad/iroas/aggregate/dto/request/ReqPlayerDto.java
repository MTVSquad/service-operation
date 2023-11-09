package com.vsquad.iroas.aggregate.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqPlayerDto {

    @Schema(name = "key", description = "플레이어 식별정보", example = "76561197960435530")
    private String key;

    @Schema(name = "type", description = "플레이어 식별 타입", allowableValues = {"steam", "local"}, example = "steam")
    private String type;
}
