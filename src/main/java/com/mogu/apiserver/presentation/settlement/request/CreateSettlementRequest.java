package com.mogu.apiserver.presentation.settlement.request;

import com.mogu.apiserver.application.settlement.request.CreateSettlementServiceRequest;
import com.mogu.apiserver.domain.settlement.enums.SettlementType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CreateSettlementRequest {
    private String bankCode;
    private String accountName;
    private String accountNumber;

    @NotNull(message = "메세지는 필수입니다.")
    private String message;

    @NotNull(message = "총 금액은 필수입니다.")
    private Long totalPrice;

    private Long userId;

    @Valid
    @NotNull(message = "정산 단계는 필수입니다.")
    private List<CreateSettlementRequestStage> settlementStage;

    @Getter
    public static class CreateSettlementRequestStage {

        @NotNull(message = "정산 단계 레벨은 필수입니다.")
        Integer level;

        @Valid
        @NotNull(message = "정산 참여자는 필수입니다.")
        List<CreateSettlementRequestParticipants> participants;

    }

    @Getter
    public static class CreateSettlementRequestParticipants {

        @NotNull(message = "정산 참여자의 이름은 필수입니다.")
        String name;

        @NotNull(message = "정산 참여자의 금액은 필수입니다.")
        Long price;

        @NotNull(message = "정산 참여자의 우선순위는 필수입니다.")
        Integer priority;

        @NotNull(message = "정산 참여자의 타입은 필수입니다.")
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
                                        .priority(participants.getPriority())
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
                .settlementStage(settlementServiceRequestStages)
                .build();

        return createSettlementServiceRequest;

    }
}

