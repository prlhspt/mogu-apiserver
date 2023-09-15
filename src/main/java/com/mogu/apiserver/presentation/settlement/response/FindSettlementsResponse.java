package com.mogu.apiserver.presentation.settlement.response;

import com.mogu.apiserver.domain.settlement.enums.SettlementStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class FindSettlementsResponse {

    private Long totalPrice;
    private String title;
    private SettlementStatus status;
    private OffsetDateTime date;



}
