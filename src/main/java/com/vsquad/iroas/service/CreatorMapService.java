package com.vsquad.iroas.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vsquad.iroas.aggregate.dto.ReqCreatorMapDto;
import com.vsquad.iroas.aggregate.entity.CreatorMap;
import com.vsquad.iroas.repository.CreatorMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorMapService {

    private final CreatorMapRepository creatorMapRepository;

    public void addCreatorMap(ReqCreatorMapDto creatorMapDto) throws JsonProcessingException {
       CreatorMap map = creatorMapDto.convertToEntity(creatorMapDto);
       creatorMapRepository.save(map);
    }


}
