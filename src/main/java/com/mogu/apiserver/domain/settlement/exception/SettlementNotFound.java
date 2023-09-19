package com.mogu.apiserver.domain.settlement.exception;

import com.mogu.apiserver.global.error.BusinessException;

public class SettlementNotFound extends BusinessException {

    public SettlementNotFound() {
        super(SettlementErrorCode.SETTLEMENT_NOT_FOUND);
    }

}
