package com.vsquad.iroas.controller;

import com.vsquad.iroas.aggregate.dto.CreatorMapDto;
import com.vsquad.iroas.aggregate.dto.ResCreatorMapDto;
import com.vsquad.iroas.aggregate.dto.ResMessageDto;
import com.vsquad.iroas.service.CreatorMapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/maps")
@Slf4j
@Tag(name = "맵 API")
public class CreatorMapController {

    private final CreatorMapService creatorMapService;

    @PostMapping
    @Operation(summary = "크리에이터 맵 추가", description = "크리에이터 맵을 추가합니다.", responses = {
            @ApiResponse(responseCode = "201", description = "맵 추가 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ResMessageDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "맵 추가 실패", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(name = "맵 추가 실패", example = "에러 메시지"), mediaType = "application/json"))
    })
    public ResponseEntity<ResMessageDto> addCreatorMap(@RequestBody CreatorMapDto reqDto) {
        try {
            creatorMapService.addCreatorMap(reqDto);
            ResMessageDto resDto = new ResMessageDto("새로운 맵 추가 완료");
            return new ResponseEntity<>(resDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResMessageDto("맵 추가 실패"));
        }
    }

    @GetMapping("/{creatorMapId}")
    @Operation(summary = "크리에이터 맵 조회", description = "크리에이터 맵을 조회합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "맵 조회 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ResCreatorMapDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "맵 조회 실패", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(name = "맵 조회 실패", example = "에러 메시지"), mediaType = "application/json"))
    })
    public ResponseEntity<ResCreatorMapDto> getCreatorMap(@PathVariable String creatorMapId) {
        try {
            CreatorMapDto resDto = creatorMapService.getCreatorMap(creatorMapId);

            ResCreatorMapDto res = new ResCreatorMapDto(resDto, "맵 조회 성공");

            log.info("맵 조회 성공");

            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            log.warn("맵 조회 실패");
            log.warn(e.getMessage());

            ResCreatorMapDto res = new ResCreatorMapDto(null, "맵 조회 실패");
            return ResponseEntity.badRequest().body(res);
        }
    }

}
