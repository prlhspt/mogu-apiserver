package com.mogu.apiserver.presentation.settlement.request;

import com.mogu.apiserver.application.settlement.request.UpdateSettlementServiceRequest;
import com.mogu.apiserver.application.settlement.request.UpdateSettlementServiceRequest.UpdateSettlementParticipantsServiceRequest;
import com.mogu.apiserver.application.settlement.request.UpdateSettlementServiceRequest.UpdateSettlementStageRequestService;
import com.mogu.apiserver.domain.settlement.enums.SettlementParticipantStatus;
import com.mogu.apiserver.domain.settlement.enums.SettlementType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class UpdateSettlementRequest {

    @Schema(description = "은행 코드", example = "004")
    private String bankCode;

    @Schema(description = "계좌 주인 이름", example = "홍길동")
    private String accountName;

    @Schema(description = "계좌 번호", example = "123456789")
    private String accountNumber;

    @Schema(description = "메세지", example = "정산 요청합니다.")
    private String message;

    @Schema(description = "총 금액", example = "10000")
    private Long totalPrice;

    @Schema(description = "정산 요청자의 ID", example = "1")
    private Long userId;

    @Schema(description = "정산 단계")
    private List<UpdateSettlementStagesRequest> settlementStage;

    @Getter
    @NoArgsConstructor
    public static class UpdateSettlementStagesRequest {

        @Schema(description = "정산 단계 id", example = "1")
        private Long id;

        @Schema(description = "정산 단계 레벨", example = "1")
        private Integer level;

        @Schema(description = "정산 참여자")
        private List<UpdateSettlementParticipantsRequest> participants;

        @Builder
        public UpdateSettlementStagesRequest(Integer level, List<UpdateSettlementParticipantsRequest> participants) {
            this.level = level;
            this.participants = participants;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateSettlementParticipantsRequest {

        @Schema(description = "정산 참여자의 id", example = "1")
        private Long id;

        @Schema(description = "정산 참여자의 이름", example = "홍길동")
        private String name;

        @Schema(description = "정산 참여자의 금액", example = "10000")
        private Long price;

        @Schema(description = "정산 참여자 순서", example = "1")
        private Integer priority;

        @Schema(description = "정산 참여자의 타입", example = "DUTCH_PAY")
        private SettlementType settlementType;

        @Schema(description = "정산 참여자의 상태", example = "WAITING")
        private SettlementParticipantStatus settlementParticipantStatus;

        @Schema(description = "정산 참여자 정산 비율", example = "15")
        private Integer percentage;

        @Builder
        public UpdateSettlementParticipantsRequest(String name, Long price, Integer priority, SettlementType settlementType, SettlementParticipantStatus settlementParticipantStatus, Integer percentage) {
            this.name = name;
            this.price = price;
            this.priority = priority;
            this.settlementType = settlementType;
            this.settlementParticipantStatus = settlementParticipantStatus;
            this.percentage = percentage;
        }
    }

    @Builder
    public UpdateSettlementRequest(String bankCode, String accountName, String accountNumber, String message, Long totalPrice, Long userId, List<UpdateSettlementStagesRequest> settlementStage) {
        this.bankCode = bankCode;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.message = message;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.settlementStage = settlementStage;
    }

    public UpdateSettlementServiceRequest toServiceRequest() {
        List<UpdateSettlementStageRequestService> settlementServiceRequestStages = settlementStage.stream()
                .map(settlementStage -> UpdateSettlementStageRequestService.builder()
                        .id(settlementStage.getId())
                        .level(settlementStage.getLevel())
                        .participants(settlementStage.getParticipants().stream()
                                .map(participants -> UpdateSettlementParticipantsServiceRequest.builder()
                                        .id(participants.getId())
                                        .name(participants.getName())
                                        .price(participants.getPrice())
                                        .priority(participants.getPriority())
                                        .settlementType(participants.getSettlementType())
                                        .settlementParticipantStatus(participants.getSettlementParticipantStatus())
                                        .percentage(participants.getPercentage())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        return UpdateSettlementServiceRequest.builder()
                .bankCode(bankCode)
                .accountName(accountName)
                .accountNumber(accountNumber)
                .message(message)
                .totalPrice(totalPrice)
                .settlementStage(settlementServiceRequestStages)
                .build();

    }
}
