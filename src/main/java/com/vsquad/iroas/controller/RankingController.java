package com.vsquad.iroas.controller;

import com.vsquad.iroas.aggregate.dto.ResRankingDto;
import com.vsquad.iroas.aggregate.dto.request.ReqRankingDto;
import com.vsquad.iroas.aggregate.dto.response.ResErrorMessage;
import com.vsquad.iroas.aggregate.dto.response.ResMessageDto;
import com.vsquad.iroas.aggregate.dto.response.ResponseDto;
import com.vsquad.iroas.aggregate.dto.response.ResponseError;
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

    @GetMapping
    @Operation(summary = "랭킹 조회", responses = {
            @ApiResponse(responseCode = "200", description = "랭킹 조회 성공"),
            @ApiResponse(responseCode = "400", description = "랭킹 조회 실패",
                    content = @Content(schema = @Schema(implementation = ResponseError.class),
                            mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "creatorMapId", description = "커스텀 모드 유즈맵 식별자", required = true),
            @Parameter(name = "page", description = "페이지 번호", example = "0"),
            @Parameter(name = "size", description = "페이지 크기", example = "10"),
            @Parameter(name = "sort", description = "정렬 기준(playCount : 플레이 횟수, clearCount : 클리어 횟수, playTime.playMilliSecond : 플레이 시간)", schema = @Schema(allowableValues = {"playCount", "clearCount", "playTime.playMilliSecond"}),
                    example = "playTime.playMilliSecond", in = ParameterIn.QUERY),
            @Parameter(name = "direction", description = "정렬 방향", schema = @Schema(allowableValues = {"asc", "desc"}), example = "asc")
    })
    public ResponseEntity<ResponseDto<Page<ResRankingDto>>> getRanking(@RequestParam @Parameter(hidden = true) String creatorMapId, @PageableDefault(sort = {"playTime.playMilliSecond"}) @Parameter(hidden = true) Pageable pageable) {
        try {
            log.info("랭킹 조회");

            Page<ResRankingDto> ranking = rankingService.getRanking(creatorMapId, pageable);

            ResponseDto resDto = new ResponseDto(ranking, "랭킹 조회 완료");

            log.info("랭킹 조회 성공");
            return new ResponseEntity<>(resDto, HttpStatus.OK);
        } catch (Exception e) {
            log.warn("랭킹 조회 실패");

            ResponseDto resDto = new ResponseDto(null, "랭킹 조회 실패");
            return ResponseEntity.badRequest().body(resDto);
        }
    }
}
