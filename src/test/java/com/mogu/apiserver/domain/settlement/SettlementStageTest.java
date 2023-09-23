package com.mogu.apiserver.domain.settlement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SettlementStageTest {

    @Test
    @DisplayName("null 로 들어온 값은 업데이트하지 않는다.")
    void updateNullValue() {
        SettlementStage settlementStage = SettlementStage.builder()
                .level(1)
                .build();

        settlementStage.updateNotNullValue(null);

        assertThat(settlementStage.getLevel()).isEqualTo(1);
    }

    @Test
    @DisplayName("null 이 아닌 값은 업데이트한다.")
    void updateNotNullValue() {
        SettlementStage settlementStage = SettlementStage.builder()
                .level(1)
                .build();

        settlementStage.updateNotNullValue(2);

        assertThat(settlementStage.getLevel()).isEqualTo(2);
    }
}