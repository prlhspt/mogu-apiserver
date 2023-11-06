package com.mogu.apiserver.presentation.authentication.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RefreshTokenResponse {

    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNjI5MjU0NjY2LCJ", description = "JWT Refresh Token")
    private String accessToken;

    @Schema(description = "User ID", example = "1")
    private Long userId;

}
