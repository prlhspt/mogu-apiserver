package com.mogu.apiserver.presentation.settlement.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateSettlementResponse {

    @Schema(description = "정산 ID", example = "1")
    private final Long settlementId;


    @Builder
    private CreateSettlementResponse(Long settlementId) {
        this.settlementId = settlementId;
    }

}
