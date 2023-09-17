package com.mogu.apiserver.presentation.settlement.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateSettlementResponse {

    private Long settlementId;


    @Builder
    private CreateSettlementResponse(Long settlementId) {
        this.settlementId = settlementId;
    }

}
