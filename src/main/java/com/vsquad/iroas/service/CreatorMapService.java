package com.vsquad.iroas.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsquad.iroas.aggregate.dto.CreatorMapDto;
import com.vsquad.iroas.aggregate.entity.CreatorMap;
import com.vsquad.iroas.aggregate.entity.Prop;
import com.vsquad.iroas.repository.CreatorMapRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreatorMapService {

    private final CreatorMapRepository creatorMapRepository;
    private final ModelMapper modelMapper;

    public void addCreatorMap(CreatorMapDto creatorMapDto) throws JsonProcessingException {
       CreatorMap map = creatorMapDto.convertToEntity(creatorMapDto);
       creatorMapRepository.save(map);
    }

    public CreatorMapDto getCreatorMap(String creatorMapId) throws IllegalArgumentException, IOException {
        CreatorMap creatorMap = creatorMapRepository.findById(creatorMapId)
                .orElseThrow(() -> new IllegalArgumentException("해당 맵이 존재하지 않습니다."));

        CreatorMapDto creatorMapDto = modelMapper.map(creatorMap, CreatorMapDto.class);

        String startPoint = creatorMap.getPlayerStartPoint();

        ObjectMapper objectMapper = new ObjectMapper();

        if(startPoint != null) {
            objectMapper.readValue(startPoint, List.class);
        }

        return creatorMapDto;
    }

}
