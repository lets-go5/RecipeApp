package com.example.recipeapp.global.exception;

import com.example.recipeapp.global.response.ApiResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handleCustomException(CustomException e) {

        ErrorCode errorCode = e.getErrorCode();
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(errorCode);

        log.warn("[클라이언트 예외 발생] {} - {}", errorCode.name(), errorCode.getMessage());

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ApiResponse.fail(errorResponseDto.getMessage(), errorResponseDto));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handleValidationException(
        MethodArgumentNotValidException e) {

        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
        String message;

        FieldError fieldError = e.getBindingResult().getFieldError();
        if (fieldError != null) {
            message = fieldError.getDefaultMessage();
        } else {
            message = "입력 값이 올바르지 않습니다.";
        }

        log.warn("[벨리데이션 예외 발생] {} - {}", errorCode.name(), message);

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
            errorCode.getStatus().value(),
            errorCode.name(),
            message);

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ApiResponse.fail(errorResponseDto.getMessage(), errorResponseDto));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {

        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
        String message = "데이터 형식이 올바르지 않습니다.";

        if (e.getCause() instanceof InvalidFormatException cause) {
            Class<?> targetType = cause.getTargetType();

            if (targetType.isEnum()) {
                message = "유효하지 않은 Enum값입니다.";
            }
        }

        log.warn("[JSON 파싱 예외 발생] {} - {}", errorCode.name(), e.getMessage());

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
            errorCode.getStatus().value(),
            errorCode.name(),
            message
        );

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ApiResponse.fail(errorResponseDto.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handleException(Exception e) {

        log.error("[알 수 없는 에러 발생]", e);

        ErrorCode unexpectedError = ErrorCode.UNEXPECTED_ERROR;
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(unexpectedError);

        return ResponseEntity
            .status(unexpectedError.getStatus())
            .body(ApiResponse.fail(errorResponseDto.getMessage(), errorResponseDto));
    }
}