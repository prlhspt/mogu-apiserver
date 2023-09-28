package com.mogu.apiserver.presentation.user.response;

import com.mogu.apiserver.domain.user.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckUserResponse {

    @Schema(description = "가입 여부", example = "true")
    private Boolean isRegistered;

    @Schema(description = "유저 상태", example = "ACTIVE")
    private UserStatus userStatus;

}
