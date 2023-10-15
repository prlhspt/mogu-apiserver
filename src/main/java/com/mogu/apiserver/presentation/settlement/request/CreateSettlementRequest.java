package com.mogu.apiserver.presentation.settlement.request;

import com.mogu.apiserver.application.settlement.request.CreateSettlementServiceRequest;
import com.mogu.apiserver.application.settlement.request.CreateSettlementServiceRequest.CreateSettlementParticipantsServiceRequest;
import com.mogu.apiserver.application.settlement.request.CreateSettlementServiceRequest.CreateSettlementStagesServiceRequest;
import com.mogu.apiserver.domain.settlement.enums.SettlementType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
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

    @Schema(description = "정산 이미지", example = "[\"https://mogu-settlement-image.s3.ap-northeast-2.amazonaws.com/dd3d2e49-c1d6-4512-91bf-6ecf857072fb.jpg\"]")
    private List<String> settlementImages;

    @Valid
    @NotNull(message = "정산 단계는 필수입니다.")
    @Schema(description = "정산 단계")
    private List<CreateSettlementStagesRequest> settlementStage;

    @Getter
    @NoArgsConstructor
    public static class CreateSettlementStagesRequest {

        @NotNull(message = "정산 단계 레벨은 필수입니다.")
        @Schema(description = "정산 단계 레벨", example = "1")
        Integer level;

        @Valid
        @NotNull(message = "정산 참여자는 필수입니다.")
        @Schema(description = "정산 참여자")
        List<CreateSettlementParticipantsRequest> participants;

        @Builder
        public CreateSettlementStagesRequest(Integer level, List<CreateSettlementParticipantsRequest> participants) {
            this.level = level;
            this.participants = participants;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CreateSettlementParticipantsRequest {

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

        @Schema(description = "정산 참여자 퍼센트 비율", example = "15")
        Integer percentage;

        @Builder
        public CreateSettlementParticipantsRequest(String name, Long price, Integer priority, SettlementType settlementType, Integer percentage) {
            this.name = name;
            this.price = price;
            this.priority = priority;
            this.settlementType = settlementType;
            this.percentage = percentage;
        }
    }

    @Builder
    public CreateSettlementRequest(String bankCode, String accountName, String accountNumber, String message, Long totalPrice, Long userId, List<String> settlementImages, List<CreateSettlementStagesRequest> settlementStage) {
        this.bankCode = bankCode;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.message = message;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.settlementImages = settlementImages;
        this.settlementStage = settlementStage;
    }

    public CreateSettlementServiceRequest toServiceRequest() {
        List<CreateSettlementStagesServiceRequest> settlementServiceRequestStages = settlementStage.stream()
                .map(settlementStage -> CreateSettlementStagesServiceRequest.builder()
                        .level(settlementStage.getLevel())
                        .participants(settlementStage.getParticipants().stream()
                                .map(participants -> CreateSettlementParticipantsServiceRequest.builder()
                                        .name(participants.getName())
                                        .price(participants.getPrice())
                                        .priority(participants.getPriority())
                                        .settlementType(participants.getSettlementType())
                                        .percentage(participants.getPercentage())
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
                .settlementImages(settlementImages)
                .settlementStage(settlementServiceRequestStages)
                .build();

    }

}

