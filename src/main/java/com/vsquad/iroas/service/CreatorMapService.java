package com.vsquad.iroas.service;

import com.vsquad.iroas.aggregate.dto.response.ResCreatorMapDto;
import com.vsquad.iroas.aggregate.dto.request.ReqCreatorMapDto;
import com.vsquad.iroas.aggregate.entity.CreatorMap;
import com.vsquad.iroas.config.exception.CreatorMapNotFoundException;
import com.vsquad.iroas.config.exception.PlayerNotFoundException;
import com.vsquad.iroas.config.token.PlayerPrincipal;
import com.vsquad.iroas.repository.CreatorMapRepository;
import com.vsquad.iroas.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
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
                        playerRepository.findById(context.getSource()).get().getNickname() : null)
                .map(CreatorMap::getCreator, ResCreatorMapDto::setCreator)).validate();   // modelMapper error 출력
    }

    public ResCreatorMapDto addCreatorMap(ReqCreatorMapDto creatorMapDto) {

        PlayerPrincipal userDetails = (PlayerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // UserDetails에서 사용자 정보 사용
        Long id = userDetails.getId();
        String nickname = userDetails.getName();

        CreatorMap map = creatorMapDto.convertToEntity(creatorMapDto);

        Integer count = creatorMapRepository.maxNumberByCreatorMapName(nickname + "의맵");

        if(count != null && !count.equals(0)) map.setCreatorMapName(nickname + "의맵" + "_" +  (count + 1));
        else map.setCreatorMapName(nickname + "의맵" + "_" + "1");

        map.setCreator(id);
        CreatorMap creatorMap = creatorMapRepository.save(map);

        initModelMapper();
        return modelMapper.map(creatorMap, ResCreatorMapDto.class);
    }

    public ResCreatorMapDto getCreatorMap(Long creatorMapId) throws IllegalArgumentException {
        CreatorMap creatorMap = creatorMapRepository.findById(creatorMapId)
                .orElseThrow(CreatorMapNotFoundException::new);

        initModelMapper();

        return modelMapper.map(creatorMap, ResCreatorMapDto.class);
    }

//    @Transactional(readOnly = true)
    public Page<ResCreatorMapDto> readPlayerCreatorMapList(String nickname, Pageable pageable) {

        Page<CreatorMap> creatorMapPage;

        if (nickname != null && !nickname.isEmpty()) {
            // 닉네임 필터가 있는 경우

            Long id = playerRepository.findByNickname_PlayerNickname(nickname)
                    .orElseThrow(() -> new PlayerNotFoundException("닉네임에 해당하는 회원이 없습니다.")).getPlayerId();

            creatorMapPage = creatorMapRepository.findByCreator(id, pageable);
        } else {
            // 닉네임 필터가 없는 경우
            creatorMapPage = creatorMapRepository.findAll(pageable);
        }

        if(creatorMapPage.isEmpty()) throw new CreatorMapNotFoundException("해당 맵 목록 없음");

        //ResCreatorMapDto 변환
        initModelMapper();

        return creatorMapPage.map(creatorMap -> modelMapper.map(creatorMap, ResCreatorMapDto.class));
    }

    @Transactional
    public void deleteCreatorMap(Long creatorMapId) {

        PlayerPrincipal userDetails = (PlayerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long userId = userDetails.getId();

        creatorMapRepository.findByCreatorMapIdAndCreator(creatorMapId, userId)
                .orElseThrow(CreatorMapNotFoundException::new);

        creatorMapRepository.deleteCreatorMapByCreatorMapIdAndCreator(creatorMapId, userId);
    }
}
