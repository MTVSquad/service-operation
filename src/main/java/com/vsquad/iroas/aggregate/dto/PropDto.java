package com.vsquad.iroas.aggregate.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsquad.iroas.aggregate.entity.Prop;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PropDto {

    private String propId;

    private String propName;

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
