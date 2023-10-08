package com.mogu.apiserver.presentation.settlement.request;

import com.mogu.apiserver.application.settlement.request.UpdateSettlementServiceRequest;
import com.mogu.apiserver.application.settlement.request.UpdateSettlementServiceRequest.UpdateSettlementParticipantsServiceRequest;
import com.mogu.apiserver.application.settlement.request.UpdateSettlementServiceRequest.UpdateSettlementStageRequestService;
import com.mogu.apiserver.domain.settlement.enums.SettlementParticipantStatus;
import com.mogu.apiserver.domain.settlement.enums.SettlementType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Builder
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
    @Builder
    public static class UpdateSettlementStagesRequest {

        @Schema(description = "정산 단계 레벨", example = "1")
        private Long id;

        @Schema(description = "정산 단계 레벨", example = "1")
        private Integer level;

        @Schema(description = "정산 참여자")
        private List<UpdateSettlementParticipantsRequest> participants;

    }

    @Getter
    @Builder
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
