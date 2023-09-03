package com.mogu.apiserver.domain.user.exception;

import com.mogu.apiserver.global.error.ErrorObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorObject {

    ALREADY_EXIST_LOGIN_ID(HttpStatus.CONFLICT, "이미 존재하는 로그인 아이디입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
