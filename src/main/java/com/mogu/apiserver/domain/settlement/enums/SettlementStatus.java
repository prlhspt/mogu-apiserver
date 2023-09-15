package com.mogu.apiserver.domain.settlement.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SettlementStatus {

    WAITING("대기"),
    DONE("완료");

    private final String description;
}
