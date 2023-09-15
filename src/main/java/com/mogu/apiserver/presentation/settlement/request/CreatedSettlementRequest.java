package com.mogu.apiserver.presentation.settlement.request;

import com.mogu.apiserver.application.settlement.request.CreateSettlementServiceRequest;
import com.mogu.apiserver.domain.settlement.enums.SettlementType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CreatedSettlementRequest {
    private String bankCode;
    private String accountName;
    private String accountNumber;

    private String message;

    private Long totalPrice;

    private List<CreateSettlementRequestStage> settlementStage;

    @Getter
    public static class CreateSettlementRequestStage {

        Integer level;
        List<CreateSettlementRequestParticipants> participants;

    }

    @Getter
    public static class CreateSettlementRequestParticipants {

        String name;
        Long price;
        SettlementType settlementType;
    }

    public CreateSettlementServiceRequest toServiceRequest() {
        List<CreateSettlementServiceRequest.CreateSettlementServiceRequestStage> settlementServiceRequestStages = settlementStage.stream()
                .map(settlementStage -> CreateSettlementServiceRequest.CreateSettlementServiceRequestStage.builder()
                        .level(settlementStage.getLevel())
                        .participants(settlementStage.getParticipants().stream()
                                .map(participants -> CreateSettlementServiceRequest.CreateSettlementServiceRequestParticipants.builder()
                                        .name(participants.getName())
                                        .price(participants.getPrice())
                                        .settlementType(participants.getSettlementType())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        CreateSettlementServiceRequest createSettlementServiceRequest = CreateSettlementServiceRequest.builder()
                .bankCode(bankCode)
                .accountName(accountName)
                .accountNumber(accountNumber)
                .message(message)
                .totalPrice(totalPrice)
                .imageURLs(imageURLs)
                .settlementStage(settlementServiceRequestStages)
                .build();

        return createSettlementServiceRequest;

    }
}

