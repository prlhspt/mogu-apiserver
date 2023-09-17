package com.mogu.apiserver.global.util;

import com.mogu.apiserver.global.pagination.PaginationResultResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponseEntity<T> {

    private final int code;
    private final HttpStatus status;
    private final String message;
    private final T data;
    private final PaginationResultResponse pagination;

    public ApiResponseEntity(HttpStatus status, String message, T data, PaginationResultResponse pagination) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
        this.pagination = pagination;
    }

    public static <T> ApiResponseEntity<T> of(HttpStatus status, String message, T data, PaginationResultResponse paginationResultResponse) {
        return new ApiResponseEntity<>(status, message, data, paginationResultResponse);
    }

    public static <T> ApiResponseEntity<T> of(HttpStatus status, T data, PaginationResultResponse paginationResultResponse) {
        return of(status, status.name(), data, paginationResultResponse);
    }

    public static <T> ApiResponseEntity<T> ok(T data) {
        return of(HttpStatus.OK, data, null);
    }

    public static <T> ApiResponseEntity<T> ok(T data, PaginationResultResponse paginationResultResponse) {
        return of(HttpStatus.OK, data, paginationResultResponse);
    }

}
