package com.mogu.apiserver.global.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponseEntity<T> {

    private final int code;
    private final HttpStatus status;
    private final String message;
    private final T data;

    public ApiResponseEntity(HttpStatus status, String message, T data) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponseEntity<T> of(HttpStatus status, String message, T data) {
        return new ApiResponseEntity<>(status, message, data);
    }

    public static <T> ApiResponseEntity<T> of(HttpStatus status, T data) {
        return of(status, status.name(), data);
    }

    public static <T> ApiResponseEntity<T> ok(T data) {
        return of(HttpStatus.OK, data);
    }

}
