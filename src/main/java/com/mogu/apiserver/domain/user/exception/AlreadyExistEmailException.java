package com.mogu.apiserver.domain.user.exception;

import com.mogu.apiserver.global.error.BusinessException;

public class AlreadyExistEmailException extends BusinessException {

    public AlreadyExistEmailException() {
        super(UserErrorCode.ALREADY_EXIST_EMAIL);
    }
}
