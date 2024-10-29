package com.vsquad.iroas.aggregate.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseDto<T> {

    @Schema(description = "응답 몸체")
    private T body;

    @Schema(description = "응답 메시지")
    private String message;

    public ResponseDto(String message) {
        this.message = message;
    }

    // 정적 팩토리 메서드
    public static <T> ResponseDto<T> success(T body, String message) {
        return new ResponseDto<>(body, message);
    }

    public static <T> ResponseDto<T> success(String message) {
        return new ResponseDto<>(message);
    }

    public static <T> ResponseDto<T> error(String message) {
        return new ResponseDto<>(null, message);
    }
}
