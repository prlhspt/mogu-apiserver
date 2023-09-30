package com.mogu.apiserver.presentation.user.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mogu.apiserver.application.user.request.UpdateUserServiceRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonDeserialize(builder = UpdateUserRequest.UpdateUserRequestBuilder.class)
public class UpdateUserRequest {

    private String nickname;

    public UpdateUserServiceRequest toServiceRequest() {
        return UpdateUserServiceRequest.builder()
                .nickname(nickname)
                .build();
    }

}
