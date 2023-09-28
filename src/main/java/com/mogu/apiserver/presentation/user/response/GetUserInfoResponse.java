package com.mogu.apiserver.presentation.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetUserInfoResponse {

    @Schema(description = "유저 닉네임", example = "testNickname")
    private String nickname;

}
