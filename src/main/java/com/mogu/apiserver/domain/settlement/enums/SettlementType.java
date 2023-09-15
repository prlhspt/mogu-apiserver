package com.mogu.apiserver.domain.settlement.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SettlementType {
    DUTCH_PAY("더치페이"),
    PERCENT("퍼센트"),
    SPECIFIC_PRICE("특정금액");

    private final String description;

}
