package com.mogu.apiserver.domain.user.exception;

import com.mogu.apiserver.global.error.BusinessException;

public class AlreadyExistLoginIdException extends BusinessException {

    public AlreadyExistLoginIdException() {
        super(UserErrorCode.ALREADY_EXIST_LOGIN_ID);
    }
}
