package com.mogu.apiserver.presentation.authentication.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountLoginResponse {

    @Schema(example = "1", description = "User ID")
    private Long userId;

    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNjI5MjU0NjY2LCJ", description = "JWT Access Token")
    private final String accessToken;

    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNjI5MjU0NjY2LCJ", description = "JWT Refresh Token")
    private final String refreshToken;

    @Builder
    public AccountLoginResponse(Long userId, String accessToken, String refreshToken) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
