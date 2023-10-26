package com.vsquad.iroas.controller;

import com.vsquad.iroas.aggregate.dto.ReqRankingDto;
import com.vsquad.iroas.aggregate.dto.ResMessageDto;
import com.vsquad.iroas.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ranking")
@Slf4j
@Tag(name = "랭킹 API")
public class RankingController {

    private final RankingService rankingService;

    @PostMapping
    @Operation(summary = "랭킹 추가", description = "랭킹을 추가합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "랭킹 추가 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ResMessageDto.class), mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "랭킹 추가 실패", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(name = "랭킹 추가 실패", example = "에러 메시지"), mediaType = "application/json"))
    })
    public ResponseEntity<ResMessageDto> addRanking(@RequestBody ReqRankingDto req) {
        try {
            log.info("랭킹 추가");

            rankingService.addRanking(req);
            ResMessageDto resDto = new ResMessageDto("랭킹 추가 완료");
            return new ResponseEntity<>(resDto, HttpStatus.OK);
        } catch (Exception e) {
            log.warn("랭킹 추가 실패");

            return ResponseEntity.badRequest().body(new ResMessageDto("랭킹 추가 실패"));
        }

    }
}
