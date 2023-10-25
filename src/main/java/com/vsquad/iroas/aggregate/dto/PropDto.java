package com.vsquad.iroas.aggregate.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsquad.iroas.aggregate.entity.Prop;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropDto {

    @Schema(name = "propId", description = "구조물 식별자")
    private String propId;

    @Schema(name = "propName", description = "구조물 이름")
    private String propName;

    @Schema(name = "propLocationX", description = "구조물 위치 x 좌표값")
    private Double propLocationX;

    @Schema(name = "propLocationY", description = "구조물 위치 y 좌표값")
    private Double propLocationY;

    @Schema(name = "propLocationZ", description = "구조물 위치 z 좌표값")
    private Double propLocationZ;

    @Schema(name = "propYawValue", description = "구조물 회전값")
    private Double propYawValue;

    public static Prop convertToEntity(PropDto propDto) throws JsonProcessingException {

        if(propDto == null) {
            return null;
        }

        Prop prop = new Prop();
        prop.setPropId(propDto.getPropId());
        prop.setPropName(propDto.getPropName());
        prop.setPropLocationX(propDto.getPropLocationX());
        prop.setPropLocationY(propDto.getPropLocationY());
        prop.setPropLocationZ(propDto.getPropLocationZ());
        prop.setPropYawValue(propDto.getPropYawValue());

        return prop;
    }
}
