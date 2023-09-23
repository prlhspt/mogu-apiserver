package com.mogu.apiserver.presentation.settlement.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateSettlementResponse {

    @Schema(description = "정산 ID", example = "1")
    private Long settlementId;

}
