package com.mogu.apiserver.presentation.user.request;

import com.mogu.apiserver.application.user.request.UpdateUserServiceRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateUserRequest {

    @Schema(example = "testNickName", description = "닉네임")
    @NotBlank(message = "닉네임은 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,16}$", message = "닉네임은 2~16자리의 영문, 숫자, 한글로 이루어져야 합니다.")
    private String nickname;

    public UpdateUserServiceRequest toServiceRequest() {
        return UpdateUserServiceRequest.builder()
                .nickname(nickname)
                .build();
    }

}
