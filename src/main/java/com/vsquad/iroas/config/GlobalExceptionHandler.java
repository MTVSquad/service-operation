package com.vsquad.iroas.config;

import com.vsquad.iroas.aggregate.dto.response.ResponseDto;
import com.vsquad.iroas.config.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<ResponseDto<String>> handlePlayerNotFoundException(PlayerNotFoundException e) {
        log.warn("플레이어 찾을 수 없음: {}", e.getMessage());
        return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SteamUserNotFoundException.class)
    public ResponseEntity<ResponseDto<String>> handleSteamUserNotFoundException(SteamUserNotFoundException e) {
        log.warn("스팀 유저 찾을 수 없음: {}", e.getMessage());
        return buildErrorResponse("스팀 유저를 찾을 수 없음", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDto<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("잘못된 요청: {}", e.getMessage());
        return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDto<String>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.warn("데이터 중복 오류: {}", e.getMessage());
        return buildErrorResponse("중복된 닉네임 혹은 스팀 키", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AvatarNotFoundException.class)
    public ResponseEntity<ResponseDto<String>> handleAvatarNotFoundException(AvatarNotFoundException e) {
        log.warn("아바타 찾을 수 없음: {}", e.getMessage());
        return buildErrorResponse("플레이어 아바타를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NicknameAlreadyExistsException.class)
    public ResponseEntity<ResponseDto<String>> handleNicknameAlreadyExistsException(NicknameAlreadyExistsException e) {
        log.warn("닉네임 중복 오류: {}", e.getMessage());
        return ResponseEntity.badRequest().body(ResponseDto.error("이미 사용 중인 닉네임입니다."));
    }

    @ExceptionHandler(CreatorMapNotFoundException.class)
    public ResponseEntity<ResponseDto<String>> handleCreatorMapNotFoundException(CreatorMapNotFoundException e) {
        log.warn("크리에이터맵 찾을 수 없음: {}", e.getMessage());
        return ResponseEntity.badRequest().body(ResponseDto.error(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception e) {
        log.error("서버 오류 발생: {}", e.getMessage(), e);

        if (e instanceof IllegalArgumentException) {
            ResponseDto<String> resDto = new ResponseDto<>("잘못된 인자 전달");
            return new ResponseEntity<>(resDto, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            ResponseDto<String> resDto = new ResponseDto<>("서버 오류가 발생했습니다.");
            return new ResponseEntity<>(resDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<ResponseDto<String>> buildErrorResponse(String message, HttpStatus status) {
        ResponseDto<String> responseDto = new ResponseDto<>(message);
        return new ResponseEntity<>(responseDto, status);
    }
}
