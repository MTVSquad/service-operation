package com.vsquad.iroas.config.exception;

public class RankingNotFoundException extends RuntimeException {
    public RankingNotFoundException() {
        super("순위에 업음");
    }

    public RankingNotFoundException(String message) {
        super(message);
    }

    public RankingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
