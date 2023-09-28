package com.mogu.apiserver.application.user.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateUserServiceRequest {

    private String nickname;

}
