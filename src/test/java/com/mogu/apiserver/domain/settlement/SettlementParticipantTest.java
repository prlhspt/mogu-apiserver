package com.mogu.apiserver.domain.settlement;

import com.mogu.apiserver.domain.settlement.enums.SettlementParticipantStatus;
import com.mogu.apiserver.domain.settlement.enums.SettlementType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SettlementParticipantTest {

    @Test
    @DisplayName("null 이 아닌 값은 업데이트한다.")
    void updateNotNullValue() {
        SettlementParticipant settlementParticipant = SettlementParticipant.builder()
                .name("name")
                .price(1000L)
                .priority(1)
                .settlementType(SettlementType.DUTCH_PAY)
                .settlementParticipantStatus(SettlementParticipantStatus.WAITING)
                .build();

        settlementParticipant.updateNotNullValue(
                "changeName",
                SettlementType.PERCENT,
                2000L,
                2,
                SettlementParticipantStatus.DONE,
                15
        );

        assertThat(settlementParticipant).extracting("name", "settlementType", "price", "priority", "settlementParticipantStatus", "percentage")
                .containsExactly("changeName", SettlementType.PERCENT, 2000L, 2, SettlementParticipantStatus.DONE, 15);

    }

    @Test
    @DisplayName("null 로 들어온 값은 업데이트하지 않는다.")
    void updateNullValue() {
        SettlementParticipant settlementParticipant = SettlementParticipant.builder()
                .name("name")
                .price(1000L)
                .priority(1)
                .settlementType(SettlementType.PERCENT)
                .settlementParticipantStatus(SettlementParticipantStatus.WAITING)
                .build();

        settlementParticipant.updateNotNullValue(
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertThat(settlementParticipant).extracting("name", "settlementType", "price", "priority", "settlementParticipantStatus")
                .containsExactly("name", SettlementType.PERCENT, 1000L, 1, SettlementParticipantStatus.WAITING);

    }
}