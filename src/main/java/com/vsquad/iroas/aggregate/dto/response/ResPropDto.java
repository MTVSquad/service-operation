package com.vsquad.iroas.aggregate.dto.response;

import com.vsquad.iroas.aggregate.entity.Prop;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResPropDto {

    @Schema(name = "propId", description = "구조물 식별자")
    private Long propId;

    @Schema(name = "propName", description = "구조물 이름")
    private String propName;

    @Schema(name = "propFilePath", description = "파일 경로")
    private String propFilePath;

    @Schema(name = "propLocationX", description = "구조물 위치 x 좌표값")
    private Double propLocationX;

    @Schema(name = "propLocationY", description = "구조물 위치 y 좌표값")
    private Double propLocationY;

    @Schema(name = "propLocationZ", description = "구조물 위치 z 좌표값")
    private Double propLocationZ;

    @Schema(name = "propYawValue", description = "구조물 회전값")
    private Double propYawValue;

    public static Prop convertToEntity(ResPropDto propDto) {

        if(propDto == null) {
            return null;
        }

        Prop prop = new Prop();
        prop.setPropName(propDto.getPropName());
        prop.setPropFilePath(propDto.getPropFilePath());
        prop.setPropLocationX(propDto.getPropLocationX());
        prop.setPropLocationY(propDto.getPropLocationY());
        prop.setPropLocationZ(propDto.getPropLocationZ());
        prop.setPropYawValue(propDto.getPropYawValue());

        return prop;
    }
}
