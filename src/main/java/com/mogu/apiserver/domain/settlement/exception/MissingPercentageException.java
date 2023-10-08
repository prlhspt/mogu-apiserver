package com.mogu.apiserver.domain.settlement.exception;

import com.mogu.apiserver.global.error.BusinessException;
import com.mogu.apiserver.global.error.ErrorObject;

public class MissingPercentageException extends BusinessException {

    public MissingPercentageException() {
        super(SettlementErrorCode.MISSING_PERCENTAGE);
    }

}
