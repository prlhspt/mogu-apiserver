package com.mogu.apiserver.global.error;

import com.mogu.apiserver.global.util.ApiResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponseEntity<Object>> bindException(BindException e) {
        log.info("bindException", e);
        return new ResponseEntity<>(
                ApiResponseEntity.of(
                        HttpStatus.BAD_REQUEST,
                        e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                        null,
                        null
                ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponseEntity<Object>> businessException(BusinessException e) {
        log.error("businessException", e);
        return new ResponseEntity<>(
                ApiResponseEntity.of(e.getErrorObject().getHttpStatus()
                        , e.getErrorObject().getMessage(),
                        null,
                        null
                ), e.getErrorObject().getHttpStatus());
    }
}
