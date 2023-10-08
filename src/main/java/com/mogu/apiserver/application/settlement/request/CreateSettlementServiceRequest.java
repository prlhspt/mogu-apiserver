package com.mogu.apiserver.application.settlement.request;

import com.mogu.apiserver.domain.settlement.enums.SettlementType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CreateSettlementServiceRequest {

    private String bankCode;
    private String accountName;
    private String accountNumber;

    private String message;

    private Long totalPrice;

    private List<CreateSettlementStagesServiceRequest> settlementStage;

    @Getter
    @Builder
    public static class CreateSettlementStagesServiceRequest {

        Integer level;
        List<CreateSettlementParticipantsServiceRequest> participants;

    }

    @Getter
    @Builder
    public static class CreateSettlementParticipantsServiceRequest {

        String name;
        Long price;
        Integer priority;
        SettlementType settlementType;
        Integer percentage;

    }

    public CreateSettlementServiceRequest(String bankCode, String accountName, String accountNumber, String message, Long totalPrice, List<CreateSettlementStagesServiceRequest> settlementStage) {
        this.bankCode = bankCode;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.message = message;
        this.totalPrice = totalPrice;
        this.settlementStage = settlementStage;
    }

}
