package com.vsquad.iroas.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vsquad.iroas.aggregate.dto.CreatorMapDto;
import com.vsquad.iroas.aggregate.dto.request.ReqCreatorMapDto;
import com.vsquad.iroas.aggregate.entity.CreatorMap;
import com.vsquad.iroas.config.token.PlayerPrincipal;
import com.vsquad.iroas.repository.CreatorMapRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreatorMapService {

    private final CreatorMapRepository creatorMapRepository;
    private final ModelMapper modelMapper;

    public void addCreatorMap(ReqCreatorMapDto creatorMapDto) throws JsonProcessingException {
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

            CreatorMap map = creatorMapDto.convertToEntity(creatorMapDto);
            map.setCreatorMapId(uuid);

            map.setCreatorMapName(nickname + "의 맵");
            map.setCreator(nickname);
            creatorMapRepository.save(map);

        } else throw new UsernameNotFoundException("플레이어 인증 정보를 찾을 수 없습니다.");
    }

    public CreatorMapDto getCreatorMap(String creatorMapId) throws IllegalArgumentException {
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
//    @ExceptionHandler(EmptyResultDataAccessException.ca)
    public void deleteCreatorMap(String creatorMapId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {

            PlayerPrincipal userDetails = (PlayerPrincipal) authentication.getPrincipal();

            String nickname = userDetails.getName();

            creatorMapRepository.findByCreatorMapIdAndCreator(creatorMapId, nickname).orElseThrow(
                    () -> new IllegalArgumentException()
            );

            creatorMapRepository.deleteById(creatorMapId);

        } else {
            throw new UsernameNotFoundException("플레이어 인증 정보를 찾을 수 없습니다.");
        }
    }
}
