package com.mogu.apiserver.presentation.settlement.request;

import com.mogu.apiserver.application.settlement.request.CreateSettlementServiceRequest;
import com.mogu.apiserver.domain.settlement.enums.SettlementType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CreateSettlementRequest {
    @Schema(description = "은행 코드", example = "004")
    private String bankCode;

    @Schema(description = "계좌 주인 이름", example = "홍길동")
    private String accountName;

    @Schema(description = "계좌 번호", example = "123456789")
    private String accountNumber;

    @NotNull(message = "메세지는 필수입니다.")
    @Schema(description = "메세지", example = "정산 요청합니다.")
    private String message;

    @NotNull(message = "총 금액은 필수입니다.")
    @Schema(description = "총 금액", example = "10000")
    private Long totalPrice;

    @Schema(description = "정산 요청자의 ID", example = "1")
    private Long userId;

    @Valid
    @NotNull(message = "정산 단계는 필수입니다.")
    @Schema(description = "정산 단계")
    private List<CreateSettlementRequestStage> settlementStage;

    @Getter
    public static class CreateSettlementRequestStage {

        @NotNull(message = "정산 단계 레벨은 필수입니다.")
        @Schema(description = "정산 단계 레벨", example = "1")
        Integer level;

        @Valid
        @NotNull(message = "정산 참여자는 필수입니다.")
        @Schema(description = "정산 참여자")
        List<CreateSettlementRequestParticipants> participants;

    }

    @Getter
    public static class CreateSettlementRequestParticipants {

        @NotNull(message = "정산 참여자의 이름은 필수입니다.")
        @Schema(description = "정산 참여자의 이름", example = "홍길동")
        String name;

        @NotNull(message = "정산 참여자의 금액은 필수입니다.")
        @Schema(description = "정산 참여자의 금액", example = "10000")
        Long price;

        @NotNull(message = "정산 참여자 순서는 필수입니다.")
        @Schema(description = "정산 참여자 순서", example = "1")
        Integer priority;

        @NotNull(message = "정산 참여자의 타입은 필수입니다.")
        @Schema(description = "정산 참여자의 타입", example = "DUTCH_PAY")
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

        return CreateSettlementServiceRequest.builder()
                .bankCode(bankCode)
                .accountName(accountName)
                .accountNumber(accountNumber)
                .message(message)
                .totalPrice(totalPrice)
                .settlementStage(settlementServiceRequestStages)
                .build();

    }
}

