package com.vsquad.iroas.config.exception;

public class IdAlreadyExistsException extends RuntimeException {
    public IdAlreadyExistsException() {super("이미 추가된 플레이어 입니다.");}
    public IdAlreadyExistsException(String message) {super(message);}
    public IdAlreadyExistsException(String message, Throwable cause) {super(message, cause);}
}
