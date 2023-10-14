package com.mogu.apiserver.presentation.user.request;

import com.mogu.apiserver.application.user.request.UpdateUserServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserRequest {

    private String nickname;

    public UpdateUserServiceRequest toServiceRequest() {
        return UpdateUserServiceRequest.builder()
                .nickname(nickname)
                .build();
    }

    @Builder
    public UpdateUserRequest(String nickname) {
        this.nickname = nickname;
    }
}
