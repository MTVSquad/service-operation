package com.vsquad.iroas.config.exception;

public class RankingNotFoundException extends RuntimeException {
    public RankingNotFoundException() {
        super("렝킹을 찾을 수 없습니다.");
    }

    public RankingNotFoundException(String message) {
        super(message);
    }

    public RankingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
