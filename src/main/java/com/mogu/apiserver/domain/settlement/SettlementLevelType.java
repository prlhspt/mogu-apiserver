package com.mogu.apiserver.domain.settlement;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SettlementLevelType {
    DUTCH_PAY("더치페이"),
    PERCENT("퍼센트"),
    SPECIFIC_PRICE("특정금액");

    private final String text;

}
