package com.mogu.apiserver.domain.account.exception;

import com.mogu.apiserver.global.error.ErrorObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AccountErrorCode implements ErrorObject {

    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "계정을 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
