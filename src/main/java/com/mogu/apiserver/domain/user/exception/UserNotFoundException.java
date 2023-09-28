package com.mogu.apiserver.domain.user.exception;

import com.mogu.apiserver.global.error.BusinessException;
import com.mogu.apiserver.global.error.ErrorObject;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super(UserErrorCode.USER_NOT_FOUND);
    }

}
