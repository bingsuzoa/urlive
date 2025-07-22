package com.urlive.global.responseFormat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ApiResponseBuilder {

    public <T> ResponseEntity<ApiResponse<T>> created(ResponseMessage message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(201, message.getMessage(), data));
    }

    public <T> ResponseEntity<ApiResponse<T>> ok(ResponseMessage message, T data) {
        return ResponseEntity.ok(new ApiResponse<>(200, message.getMessage(), data));
    }

    public <Void> ResponseEntity<ApiResponse<Void>> ok(ResponseMessage message) {
        return ResponseEntity.ok(new ApiResponse<>(200, message.getMessage(), null));
    }

    public <Void> ResponseEntity<ApiResponse<Void>> badRequest(ResponseMessage message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(400, message.getMessage(), null));
    }
}
