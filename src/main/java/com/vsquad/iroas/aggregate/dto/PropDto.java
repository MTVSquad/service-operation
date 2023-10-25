package com.vsquad.iroas.aggregate.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsquad.iroas.aggregate.entity.Prop;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropDto {

    @Schema(name = "propId", description = "구조물 식별자")
    private String propId;

    @Schema(name = "propName", description = "구조물 이름")
    private String propName;

    @Schema(name = "propLocation", description = "구조물 위치(x, y, z 좌표값, 회전값 포함)", example = "[0.0, 0.0, 0.0, 0.0]")
    private List<Float> propLocation;

    public static Prop convertToEntity(PropDto propDto) throws JsonProcessingException {

        if(propDto == null) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String propLocation = objectMapper.writeValueAsString(propDto.getPropLocation());

        Prop prop = new Prop();
        prop.setPropId(propDto.getPropId());
        prop.setPropName(propDto.getPropName());
        prop.setPropLocation(propLocation);

        return prop;
    }
}
