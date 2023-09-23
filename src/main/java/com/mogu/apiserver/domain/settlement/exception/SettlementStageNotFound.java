package com.mogu.apiserver.domain.settlement.exception;

import com.mogu.apiserver.global.error.BusinessException;

public class SettlementStageNotFound extends BusinessException {

    public SettlementStageNotFound() {
        super(SettlementErrorCode.SETTLEMENT_STAGE_NOT_FOUND);
    }

}
