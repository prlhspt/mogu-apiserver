package com.mogu.apiserver.domain.nonuser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NonUserStatus {
    ACTIVE("활성화"),
    INACTIVE("비활성화");

    private final String text;
}
