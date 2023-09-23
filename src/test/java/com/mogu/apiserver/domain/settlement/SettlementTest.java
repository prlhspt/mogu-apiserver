package com.mogu.apiserver.domain.settlement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SettlementTest {

    @Test
    @DisplayName("null 이 아닌 값은 업데이트한다.")
    void updateNotNullValue() {
        Settlement settlement = Settlement.builder()
                .bankCode("bankCode")
                .accountNumber("accountNumber")
                .accountName("accountName")
                .message("message")
                .totalPrice(1000L)
                .build();

        settlement.updateNotNullValue(
                "changeBankCode",
                "changeAccountNumber",
                "changeAccountName",
                "changeMessage",
                2000L
        );

        assertThat(settlement).extracting("bankCode", "accountName", "accountNumber", "message", "totalPrice")
                .containsExactly("changeBankCode", "changeAccountName", "changeAccountNumber", "changeMessage", 2000L);

    }

    @Test
    @DisplayName("null 로 들어온 값은 업데이트하지 않는다.")
    void updateNullValue() {
        Settlement settlement = Settlement.builder()
                .bankCode("bankCode")
                .accountNumber("accountNumber")
                .accountName("accountName")
                .message("message")
                .totalPrice(1000L)
                .build();

        settlement.updateNotNullValue(
                null,
                null,
                null,
                null,
                null
                );

        assertThat(settlement).extracting("bankCode", "accountName", "accountNumber", "message", "totalPrice")
                .containsExactly("bankCode", "accountName", "accountNumber", "message", 1000L);

    }
}