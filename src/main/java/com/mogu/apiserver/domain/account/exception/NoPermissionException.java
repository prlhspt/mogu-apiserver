package com.mogu.apiserver.domain.account.exception;

import com.mogu.apiserver.global.error.BusinessException;

public class NoPermissionException extends BusinessException {

    public NoPermissionException() {
        super(AccountErrorCode.NO_PERMISSION);
    }

}
