package com.mogu.apiserver.application.settlement.request;

import com.mogu.apiserver.domain.settlement.enums.SettlementParticipantStatus;
import com.mogu.apiserver.domain.settlement.enums.SettlementType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Getter
@Builder
public class UpdateSettlementServiceRequest {

    private String bankCode;

    private String accountName;

    private String accountNumber;

    private String message;

    private Long totalPrice;

    private Long userId;

    private List<UpdateSettlementStageRequestService> settlementStage;

    @Getter
    @Builder
    public static class UpdateSettlementStageRequestService {

        private Long id;

        private Integer level;

        private List<UpdateSettlementParticipantsServiceRequest> participants;

    }

    @Getter
    @Builder
    public static class UpdateSettlementParticipantsServiceRequest {

        private Long id;

        private String name;

        private Long price;

        private Integer priority;

        private SettlementType settlementType;

        private SettlementParticipantStatus settlementParticipantStatus;

        private Integer percentage;

    }

}
