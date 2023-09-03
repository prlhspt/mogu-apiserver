package com.mogu.apiserver.domain.user.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserStatus {
    ACTIVE("활성화"),
    INACTIVE("비활성화");

    private final String description;
}
