package com.vsquad.iroas.config.exception;

public class AvatarNotFoundException extends RuntimeException {

    public AvatarNotFoundException() {
        super("아바타를 찾을 수 없습니다.");
    }

    public AvatarNotFoundException(String message) {
        super(message);
    }
}
