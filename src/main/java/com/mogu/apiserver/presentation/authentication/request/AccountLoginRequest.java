package com.mogu.apiserver.presentation.authentication.request;

import com.mogu.apiserver.application.authentication.request.AccountLoginServiceRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountLoginRequest {

    @Schema(example = "test@test.com", description = "이메일")
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Schema(example = "test1234", description = "비밀번호")
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$")
    private String password;

    public AccountLoginServiceRequest toServiceRequest() {
        return AccountLoginServiceRequest.builder()
                .email(email)
                .password(password)
                .build();
    }
}
