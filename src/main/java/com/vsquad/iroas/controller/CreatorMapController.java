package com.vsquad.iroas.controller;

import com.vsquad.iroas.aggregate.dto.CreatorMapDto;
import com.vsquad.iroas.aggregate.dto.ResCreatorMapDto;
import com.vsquad.iroas.aggregate.dto.ResMessageDto;
import com.vsquad.iroas.aggregate.dto.ResponseDto;
import com.vsquad.iroas.service.CreatorMapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

    @GetMapping
    @Operation(summary = "크리에이터 맵 목록 조회", description = "정렬 값, 정렬 순서, 페이지 당 요소 수, 페이지 번호를 입력, 조건에 맞는 요소 목록 조회", responses = {
            @ApiResponse(responseCode = "200", description = "맵 목록 조회 성공", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Page.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "맵 목록 조회 실패", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(name = "맵 목록 조회 실패", example = "에러 메시지"), mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "size", description = "화면에 보여줄 요소의 숫자를 결정합니다.", example = "10"),
            @Parameter(name = "offset", description = "해당 페이지 첫 번째 원소의 수", example = "1"),
            @Parameter(name = "sort", description = "정렬 기준", schema = @Schema(allowableValues = {"createTime", "creatorMapId", "creatorMapName", "creatorId"}),
                    example = "createTime", in = ParameterIn.QUERY),
            @Parameter(name = "direction", description = "정렬 방향", schema = @Schema(allowableValues = {"asc", "desc"}), example = "asc")
    })
    public ResponseEntity<ResponseDto> getCreatorMapList(@PageableDefault @Parameter(hidden = true) Pageable pageable) {
        try {
            log.info("맵 목록 조회");

            Page<CreatorMapDto> resDto = creatorMapService.readPlayerCreatorMapList(pageable);
            ResponseDto res = new ResponseDto(resDto, "맵 목록 조회 성공");

            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            log.warn("맵 목록 조회 실패");

            ResponseDto res = new ResponseDto(null, "맵 목록 조회 실패");
            return ResponseEntity.badRequest().body(res);
        }
    }

}
