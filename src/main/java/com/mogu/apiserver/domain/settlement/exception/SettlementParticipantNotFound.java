package com.mogu.apiserver.domain.settlement.exception;

import com.mogu.apiserver.global.error.BusinessException;

public class SettlementParticipantNotFound extends BusinessException {

    public SettlementParticipantNotFound() {
        super(SettlementErrorCode.SETTLEMENT_PARTICIPANT_NOT_FOUND);
    }

}
