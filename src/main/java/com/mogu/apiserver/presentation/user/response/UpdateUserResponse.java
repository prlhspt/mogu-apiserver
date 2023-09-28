package com.mogu.apiserver.presentation.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateUserResponse {

    @Schema(description = "유저 ID", example = "1")
    private Long userId;

}
