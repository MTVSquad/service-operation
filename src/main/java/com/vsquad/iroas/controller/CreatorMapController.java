package com.vsquad.iroas.controller;

import com.vsquad.iroas.aggregate.dto.ReqCreatorMapDto;
import com.vsquad.iroas.aggregate.dto.ResMessageDto;
import com.vsquad.iroas.service.CreatorMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class CreatorMapController {

    private final CreatorMapService creatorMapService;

    @PostMapping("/creator")
    public ResponseEntity<ResMessageDto> addCreatorMap(@RequestBody ReqCreatorMapDto reqDto) {
        try {
            creatorMapService.addCreatorMap(reqDto);
            return ResponseEntity.ok(new ResMessageDto("맵 추가 성공"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResMessageDto("맵 추가 실패"));
        }
    }
}
