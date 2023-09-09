package com.mogu.apiserver.domain.user.exception;

import com.mogu.apiserver.global.error.BusinessException;

public class InactivateUserException extends BusinessException {
    public InactivateUserException() {
        super(UserErrorCode.INACTIVATE_USER);
    }
}
