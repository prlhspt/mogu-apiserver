package com.mogu.apiserver.application.authentication.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterServiceRequest {

    private String email;

    private String password;

    private String nickname;

}
