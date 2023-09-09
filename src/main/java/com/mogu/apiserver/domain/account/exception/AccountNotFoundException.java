package com.mogu.apiserver.domain.account.exception;

import com.mogu.apiserver.global.error.BusinessException;

public class AccountNotFoundException extends BusinessException {

    public AccountNotFoundException() {
        super(AccountErrorCode.ACCOUNT_NOT_FOUND);
    }
}
