package com.mogu.apiserver.presentation.authentication.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterResponse {

    @Schema(example = "1", description = "User ID")
    private final Long userId;

    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNjI5MjU0NjY4L", description = "JWT Access Token")
    private final String accessToken;

    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNjI5MjU0NjY4L", description = "JWT Refresh Token")
    private final String refreshToken;

    @Builder
    public RegisterResponse(Long userId, String accessToken, String refreshToken) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
