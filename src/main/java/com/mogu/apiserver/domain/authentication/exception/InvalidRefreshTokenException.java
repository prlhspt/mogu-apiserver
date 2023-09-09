package com.mogu.apiserver.domain.authentication.exception;

import com.mogu.apiserver.global.error.BusinessException;

public class InvalidRefreshTokenException extends BusinessException {

    public InvalidRefreshTokenException() {
        super(AuthenticationErrorCode.INVALID_REFRESH_TOKEN);
    }
}
