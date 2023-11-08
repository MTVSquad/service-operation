package com.vsquad.iroas.aggregate.dto.response;

import com.vsquad.iroas.aggregate.dto.CreatorMapDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResCreatorMapDto {

    @Schema(name = "creatorMapDto", description = "맵, 에너미 스포너, 에너미 정보")
    private CreatorMapDto creatorMapDto;

    @Schema(name = "message", description = "성공 or 실패 메시지")
    private String message;
}
