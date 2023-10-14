package com.mogu.apiserver.domain.settlement.exception;

import com.mogu.apiserver.global.error.BusinessException;

public class PercentageMismatchException extends BusinessException {

    public PercentageMismatchException() {
        super(SettlementErrorCode.PERCENTAGE_MISMATCH);
    }

}
