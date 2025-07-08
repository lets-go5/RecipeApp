package com.example.recipeapp.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {

    private final int status;
    private final String errorCode;
    private final String message;

    public ErrorResponseDto(ErrorCode errorCode) {
        this.status = errorCode.getStatus().value();
        this.errorCode = errorCode.name();
        this.message = errorCode.getMessage();
    }
}