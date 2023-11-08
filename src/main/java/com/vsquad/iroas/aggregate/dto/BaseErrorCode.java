package com.vsquad.iroas.aggregate.dto;

public interface BaseErrorCode {
    public ErrorReason getErrorReason();

    String getExplainError() throws NoSuchFieldException;
}
