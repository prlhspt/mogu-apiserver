package com.mogu.apiserver.presentation.authentication.request;

import com.mogu.apiserver.application.authentication.request.RegisterServiceRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterRequest {

    @Schema(example = "test@test.com", description = "이메일")
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Schema(example = "test1234!", description = "비밀번호")
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$", message = "비밀번호는 8~20자리의 영문, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    @Schema(example = "testNickName", description = "닉네임")
    @NotBlank(message = "닉네임은 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,16}$", message = "닉네임은 2~16자리의 영문, 숫자, 한글로 이루어져야 합니다.")
    private String nickname;
    public RegisterServiceRequest toServiceRequest() {
        return RegisterServiceRequest.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }

    @Builder
    public RegisterRequest(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
