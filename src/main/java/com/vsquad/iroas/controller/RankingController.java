package com.vsquad.iroas.controller;

import com.vsquad.iroas.aggregate.dto.request.ReqRankingDto;
import com.vsquad.iroas.aggregate.dto.response.*;
import com.vsquad.iroas.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ranking")
@Slf4j
@Tag(name = "랭킹 API")
public class RankingController {

    private final RankingService rankingService;

    @PostMapping
    @Operation(summary = "랭킹 추가", description = "랭킹을 추가합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "랭킹 추가 성공", content = @Content(schema = @Schema(implementation = ResMessageDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "랭킹 추가 실패", content = @Content(schema = @Schema(implementation = ResErrorMessage.class), mediaType = "application/json"))
    })
    public ResponseEntity<ResponseDto<?>> addRanking(@RequestBody ReqRankingDto req) {
        log.info("랭킹 추가 시도 : {}", req.getCreator());

        ResRankingDto body = rankingService.addRanking(req);

        log.info("랭킹 추가 성공 : {}", req.getCreator());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success(body, "랭킹 추가 성공"));
    }

    @GetMapping
    @Operation(summary = "랭킹 조회", responses = {
            @ApiResponse(responseCode = "200", description = "랭킹 조회 성공"),
            @ApiResponse(responseCode = "400", description = "랭킹 조회 실패",
                    content = @Content(schema = @Schema(implementation = ResponseError.class),
                            mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "creatorMapId", description = "커스텀 모드 유즈맵 식별자", required = true, example = "1"),
            @Parameter(name = "page", description = "페이지 번호", example = "0"),
            @Parameter(name = "size", description = "페이지 크기", example = "10"),
            @Parameter(name = "sort", description = "정렬 기준(playCount : 플레이 횟수, clearCount : 클리어 횟수, playTime.playElapsedTime : 플레이 시간)", schema = @Schema(allowableValues = {"playCount", "clearCount", "playTime.playElapsedTime"}),
                    example = "playTime.playElapsedTime", in = ParameterIn.QUERY),
            @Parameter(name = "direction", description = "정렬 방향", schema = @Schema(allowableValues = {"asc", "desc"}), example = "asc")
    })
    public ResponseEntity<ResponseDto<Page<?>>> getRanking(@RequestParam @Parameter(hidden = true) Long creatorMapId, @PageableDefault(sort = {"playTime.playElapsedTime"}) @Parameter(hidden = true) Pageable pageable) {
        log.info("크리에이터맵 랭킹 조회 시도 : {}", creatorMapId);

        Page<ResRankingDto> body = rankingService.getRanking(creatorMapId, pageable);

        log.info("랭킹 조회 성공");
        return ResponseEntity.ok(ResponseDto.success(body, "랭킹 조회 성공"));
    }
}
