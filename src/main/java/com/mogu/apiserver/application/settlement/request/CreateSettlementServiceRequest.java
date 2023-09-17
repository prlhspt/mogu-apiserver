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

    private List<CreateSettlementServiceRequestStage> settlementStage;

    @Getter
    @Builder
    public static class CreateSettlementServiceRequestStage {

        Integer level;
        List<CreateSettlementServiceRequestParticipants> participants;

    }

    @Getter
    @Builder
    public static class CreateSettlementServiceRequestParticipants {

        String name;
        Long price;
        Integer priority;
        SettlementType settlementType;

    }

    public CreateSettlementServiceRequest(String bankCode, String accountName, String accountNumber, String message, Long totalPrice, List<CreateSettlementServiceRequestStage> settlementStage) {
        this.bankCode = bankCode;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.message = message;
        this.totalPrice = totalPrice;
        this.settlementStage = settlementStage;
    }

}
