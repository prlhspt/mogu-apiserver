package com.mogu.apiserver.application.settlement.request;

import com.mogu.apiserver.domain.settlement.enums.SettlementType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreateSettlementServiceRequest {

    private String bankCode;
    private String accountName;
    private String accountNumber;

    private String message;

    private List<String> settlementImages;

    private List<CreateSettlementStagesServiceRequest> settlementStage;

    @Getter
    @Builder
    public static class CreateSettlementStagesServiceRequest {

        Integer level;
        Long totalPrice;
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

    @Builder
    public CreateSettlementServiceRequest(String bankCode, String accountName, String accountNumber, String message, List<String> settlementImages, List<CreateSettlementStagesServiceRequest> settlementStage) {
        this.bankCode = bankCode;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.message = message;
        this.settlementImages = settlementImages;
        this.settlementStage = settlementStage;
    }
}
