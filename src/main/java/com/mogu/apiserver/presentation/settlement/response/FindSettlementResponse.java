package com.mogu.apiserver.presentation.settlement.response;

import com.mogu.apiserver.domain.settlement.Settlement;
import com.mogu.apiserver.domain.settlement.SettlementParticipant;
import com.mogu.apiserver.domain.settlement.SettlementStage;
import com.mogu.apiserver.domain.settlement.enums.SettlementType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@Builder
public class FindSettlementResponse {

    private String bankCode;
    private String accountName;
    private String accountNumber;

    private String message;

    private Long totalPrice;

    private Long userId;

    private List<FindSettlementStageResponse> settlementStages;

    public static FindSettlementResponse of(Settlement settlement) {
        return FindSettlementResponse.builder()
                .bankCode(settlement.getBankCode())
                .accountName(settlement.getAccountName())
                .accountNumber(settlement.getAccountNumber())
                .message(settlement.getMessage())
                .totalPrice(settlement.getTotalPrice())
                .userId(settlement.getUser().getId())
                .settlementStages(FindSettlementStageResponse.of(settlement.getSettlementStages()))
                .build();
    }

    @Getter
    @Builder
    public static class FindSettlementStageResponse {
        private Integer level;
        private List<FindSettlementParticipantResponse> participants;

        public static List<FindSettlementStageResponse> of(List<SettlementStage> settlementStages) {
            return settlementStages.stream()
                    .map(settlementStage -> FindSettlementStageResponse.builder()
                            .level(settlementStage.getLevel())
                            .participants(FindSettlementParticipantResponse.of(settlementStage.getSettlementParticipants()))
                            .build())
                    .collect(toList());
        }

    }

    @Getter
    @Builder
    public static class FindSettlementParticipantResponse {
        private String name;
        private Long price;
        private Integer priority;
        private SettlementType settlementType;


        public static List<FindSettlementParticipantResponse> of(List<SettlementParticipant> participants) {
            return participants.stream()
                    .map(participant -> FindSettlementParticipantResponse.builder()
                            .name(participant.getName())
                            .price(participant.getPrice())
                            .priority(participant.getPriority())
                            .settlementType(participant.getSettlementType())
                            .build())
                    .collect(toList());
        }
    }

}
