package com.mogu.apiserver.presentation.settlement.response;

import com.mogu.apiserver.domain.settlement.Settlement;
import com.mogu.apiserver.domain.settlement.SettlementParticipant;
import com.mogu.apiserver.domain.settlement.SettlementStage;
import com.mogu.apiserver.domain.settlement.enums.SettlementType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@Builder
public class FindSettlementResponse {

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

    @Schema(description = "정산 이미지", example = "[\"https://mogu-settlement-image.s3.ap-northeast-2.amazonaws.com/dd3d2e49-c1d6-4512-91bf-6ecf857072fb.jpg\"]")
    private List<String> settlementImages;

    @Schema(description = "정산 단계")
    private List<FindSettlementStageResponse> settlementStages;

    public static FindSettlementResponse of(Settlement settlement) {
        return FindSettlementResponse.builder()
                .bankCode(settlement.getBankCode())
                .accountName(settlement.getAccountName())
                .accountNumber(settlement.getAccountNumber())
                .message(settlement.getMessage())
                .totalPrice(settlement.getTotalPrice())
                .userId(settlement.getUser().getId())
                .settlementImages((settlement.getSettlementImages() != null)
                        ? settlement.getSettlementImages().stream()
                        .map(settlementImage -> settlementImage.getImagePath())
                        .collect(toList())
                        : null)
                .settlementStages(FindSettlementStageResponse.of(settlement.getSettlementStages()))
                .build();
    }

    @Getter
    @Builder
    public static class FindSettlementStageResponse {

        @Schema(description = "정산 단계 ID", example = "1")
        private Long id;

        @Schema(description = "정산 단계 레벨", example = "1")
        private Integer level;

        @Schema(description = "정산 참여자")
        private List<FindSettlementParticipantResponse> participants;

        public static List<FindSettlementStageResponse> of(List<SettlementStage> settlementStages) {
            return settlementStages.stream()
                    .map(settlementStage -> FindSettlementStageResponse.builder()
                            .id(settlementStage.getId())
                            .level(settlementStage.getLevel())
                            .participants(FindSettlementParticipantResponse.of(settlementStage.getSettlementParticipants()))
                            .build())
                    .collect(toList());
        }

    }

    @Getter
    @Builder
    public static class FindSettlementParticipantResponse {

        @Schema(description = "정산 참여자 ID", example = "1")
        private Long id;

        @Schema(description = "정산 참여자의 이름", example = "홍길동")
        private String name;

        @Schema(description = "정산 참여자의 금액", example = "10000")
        private Long price;

        @Schema(description = "정산 참여자 순서", example = "1")
        private Integer priority;

        @Schema(description = "정산 참여자의 정산 타입", example = "DUTCH_PAY")
        private SettlementType settlementType;


        public static List<FindSettlementParticipantResponse> of(List<SettlementParticipant> participants) {
            return participants.stream()
                    .map(participant -> FindSettlementParticipantResponse.builder()
                            .id(participant.getId())
                            .name(participant.getName())
                            .price(participant.getPrice())
                            .priority(participant.getPriority())
                            .settlementType(participant.getSettlementType())
                            .build())
                    .collect(toList());
        }
    }

}
