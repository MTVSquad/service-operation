package com.vsquad.iroas.config.exception;

public class CreatorMapNotFoundException extends RuntimeException {

    public CreatorMapNotFoundException() {
        super("해당 맵이 존재하지 않습니다.");
    }

    public CreatorMapNotFoundException(String message) {
        super(message);
    }
}
