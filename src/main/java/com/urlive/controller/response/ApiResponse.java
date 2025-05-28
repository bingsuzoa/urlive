package com.urlive.controller.response;

public class ApiResponse<T> {

    public ApiResponse(
            int code,
            String message,
            T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private int code;
    private String message;
    private T data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
