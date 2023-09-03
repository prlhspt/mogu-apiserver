package com.mogu.apiserver.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read", "관리자 읽기 권한"),
    ADMIN_UPDATE("admin:update", "관리자 수정 권한"),
    ADMIN_CREATE("admin:create", "관리자 생성 권한"),
    ADMIN_DELETE("admin:delete", "관리자 삭제 권한"),
    USER_READ("user:read", "유저 읽기 권한"),
    USER_UPDATE("user:update", "유저 수정 권한"),
    USER_CREATE("user:create", "유저 생성 권한"),
    USER_DELETE("user:delete", "유저 삭제 권한");

    @Getter
    private final String permission;
    private final String description;
}
