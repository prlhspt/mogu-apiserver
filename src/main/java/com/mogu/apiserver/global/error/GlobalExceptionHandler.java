package com.mogu.apiserver.global.error;

import com.mogu.apiserver.global.util.ApiResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ApiResponseEntity<Object> bindException(BindException e) {
        return ApiResponseEntity.of(
                HttpStatus.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                null);
    }

    @ExceptionHandler(BusinessException.class)
    public ApiResponseEntity<Object> businessException(BusinessException e) {
        log.error("businessException", e);
        return ApiResponseEntity.of(e.getErrorObject().getHttpStatus(), e.getErrorObject().getMessage(), null);
    }

//    @ExceptionHandler(BadCredentialsException.class)
//    public ApiResponseEntity<Object> badCredentialsException(BadCredentialsException e) {
//        log.error("badCredentialsException", e);
//        return ApiResponseEntity.of(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
//    }
}
