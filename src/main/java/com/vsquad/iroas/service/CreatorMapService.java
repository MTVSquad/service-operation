package com.vsquad.iroas.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vsquad.iroas.aggregate.dto.response.ResCreatorMapDto;
import com.vsquad.iroas.aggregate.dto.request.ReqCreatorMapDto;
import com.vsquad.iroas.aggregate.entity.CreatorMap;
import com.vsquad.iroas.config.token.PlayerPrincipal;
import com.vsquad.iroas.repository.CreatorMapRepository;
import com.vsquad.iroas.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreatorMapService {

    private final CreatorMapRepository creatorMapRepository;
    private final PlayerRepository playerRepository;
    private final ModelMapper modelMapper;

    private void initModelMapper() {
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        // modelMapper의 파라미터 타입 변환 후 원하는 값으로 변환 처리, validate() : 에러 출력
        modelMapper.typeMap(CreatorMap.class, ResCreatorMapDto.class)
                .addMappings(mapper -> mapper.using((Converter<Long, String>) context -> context.getSource() != null ?
                        playerRepository.findById(context.getSource()).get().getNickname().getPlayerNickname() : null)
                .map(CreatorMap::getCreator, ResCreatorMapDto::setCreator)).validate();   // modelMapper error 출력
    }

    public void addCreatorMap(ReqCreatorMapDto creatorMapDto) throws JsonProcessingException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {

            PlayerPrincipal userDetails = (PlayerPrincipal) authentication.getPrincipal();

            // UserDetails에서 사용자 정보 사용
            Long id = userDetails.getId();
            String nickname = userDetails.getName();

            CreatorMap map = creatorMapDto.convertToEntity(creatorMapDto);

            Integer count = creatorMapRepository.maxNumberByCreatorMapName(nickname + "의맵");

            if(count != null && !count.equals(0)) {
                map.setCreatorMapName(nickname + "의맵" + "_" +  (count + 1));
            } else {
                map.setCreatorMapName(nickname + "의맵" + "_" + "1");
            }

            map.setCreator(id);
            creatorMapRepository.save(map);

        } else throw new UsernameNotFoundException("플레이어 인증 정보를 찾을 수 없습니다.");
    }

    public ResCreatorMapDto getCreatorMap(Long creatorMapId) throws IllegalArgumentException {
        CreatorMap creatorMap = creatorMapRepository.findById(creatorMapId)
                .orElseThrow(() -> new IllegalArgumentException("해당 맵이 존재하지 않습니다."));

        initModelMapper();

        ResCreatorMapDto resCreatorMapDto = modelMapper.map(creatorMap, ResCreatorMapDto.class);

        return resCreatorMapDto;
    }

//    @Transactional(readOnly = true)
    public Page<ResCreatorMapDto> readPlayerCreatorMapList(Pageable pageable) {

        Page<CreatorMap> creatorMapPage = creatorMapRepository.findAll(pageable);

        if(creatorMapPage.isEmpty()) {
            throw new IllegalArgumentException("해당 맵 목록 없음");
        }

        initModelMapper();

        Page<ResCreatorMapDto> creatorMapDtoPage = creatorMapPage.map(creatorMap -> {

            ResCreatorMapDto creatorMapDto = modelMapper.map(creatorMap, ResCreatorMapDto.class);

            return creatorMapDto;
        });

        return creatorMapDtoPage;
    }

    @Transactional
    public void deleteCreatorMap(Long creatorMapId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {

            PlayerPrincipal userDetails = (PlayerPrincipal) authentication.getPrincipal();

            Long userId = userDetails.getId();

            creatorMapRepository.findByCreatorMapIdAndCreator(creatorMapId, userId).orElseThrow(
                    () -> new IllegalArgumentException()
            );

            creatorMapRepository.deleteCreatorMapByCreatorMapIdAndCreator(creatorMapId, userId);
        } else {
            throw new UsernameNotFoundException("플레이어 인증 정보를 찾을 수 없습니다.");
        }
    }
}
