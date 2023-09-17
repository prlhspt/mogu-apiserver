package com.mogu.apiserver.domain.settlement.exception;

import com.mogu.apiserver.global.error.BusinessException;
import com.mogu.apiserver.global.error.ErrorObject;

public class SettlementNotFound extends BusinessException {

    public SettlementNotFound() {
        super(SettlementErrorCode.SETTLEMENT_NOT_FOUND);
    }

}
