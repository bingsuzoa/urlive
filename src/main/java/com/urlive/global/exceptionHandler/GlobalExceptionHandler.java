package com.urlive.global.exceptionHandler;

import com.urlive.global.responseFormat.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException e) {
        ApiResponse<Void> response = new ApiResponse<>(500, e.getMessage(), null);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgument(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError) {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            } else {
                errors.put(error.getObjectName(), error.getDefaultMessage());
            }
        });

        String mainErrorMessage;
        if (errors.isEmpty()) {
            mainErrorMessage = "유효성 검사에 실패했습니다.";
        } else {
            mainErrorMessage = errors.values().stream()
                    .collect(Collectors.joining("; "));
        }
        ApiResponse<Map<String, String>> response = new ApiResponse<>(400, mainErrorMessage, errors);
        return ResponseEntity.badRequest().body(response);
    }
}
