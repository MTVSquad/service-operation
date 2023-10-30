package com.vsquad.iroas.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vsquad.iroas.aggregate.dto.CreatorMapDto;
import com.vsquad.iroas.aggregate.entity.CreatorMap;
import com.vsquad.iroas.aggregate.entity.Prop;
import com.vsquad.iroas.config.token.PlayerPrincipal;
import com.vsquad.iroas.repository.CreatorMapRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreatorMapService {

    private final CreatorMapRepository creatorMapRepository;
    private final ModelMapper modelMapper;

    public void addCreatorMap(CreatorMapDto creatorMapDto) throws JsonProcessingException {
        String uuid;

        while (true) {
            uuid = UUID.randomUUID().toString();
            Optional<CreatorMap> isCreatorMap = creatorMapRepository.findById(uuid);

            if (!isCreatorMap.isPresent()) {
                break;
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {

            PlayerPrincipal userDetails = (PlayerPrincipal) authentication.getPrincipal();

            // UserDetails에서 사용자 정보 사용
            String nickname = userDetails.getName();

//            creatorMapDto.setCreatorMapId(uuid);
//            creatorMapDto.setCreator(nickname);

            CreatorMap map = creatorMapDto.convertToEntity(creatorMapDto);
            map.setCreatorMapId(uuid);
            // 임시
            map.setCreatorMapName(nickname + "의 맵");
            map.setCreator(nickname);
            creatorMapRepository.save(map);
        }
    }

    public CreatorMapDto getCreatorMap(String creatorMapId) throws IllegalArgumentException, IOException {
        CreatorMap creatorMap = creatorMapRepository.findById(creatorMapId)
                .orElseThrow(() -> new IllegalArgumentException("해당 맵이 존재하지 않습니다."));

        CreatorMapDto creatorMapDto = modelMapper.map(creatorMap, CreatorMapDto.class);

        return creatorMapDto;
    }

    @Transactional(readOnly = true)
    public Page<CreatorMapDto> readPlayerCreatorMapList(Pageable pageable) {

        Page<CreatorMap> creatorMapPage = creatorMapRepository.findAll(pageable);

        if(creatorMapPage.isEmpty()) {
            throw new IllegalArgumentException("해당 맵 목록 없음");
        }

        Page<CreatorMapDto> creatorMapDtoPage = creatorMapPage.map(creatorMap -> modelMapper.map(creatorMap, CreatorMapDto.class));

        return creatorMapDtoPage;
    }

    @Transactional
    public void deleteCreatorMap(String creatorMapId) {
        creatorMapRepository.deleteById(creatorMapId);
    }
}
