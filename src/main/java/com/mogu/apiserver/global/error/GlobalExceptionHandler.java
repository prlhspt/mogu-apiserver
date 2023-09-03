package com.mogu.apiserver.global.error;

import com.mogu.apiserver.global.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                null);
    }

    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Object> businessException(BusinessException e) {
        log.error("businessException", e);
        return ApiResponse.of(e.getErrorObject().getHttpStatus(), e.getErrorObject().getMessage(), null);
    }

}
