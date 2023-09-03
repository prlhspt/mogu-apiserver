package com.mogu.apiserver.presentation.authentication;

import com.mogu.apiserver.application.authentication.AuthenticationService;
import com.mogu.apiserver.global.util.ApiResponse;
import com.mogu.apiserver.presentation.authentication.request.AccountLoginRequest;
import com.mogu.apiserver.presentation.authentication.request.RegisterRequest;
import com.mogu.apiserver.presentation.authentication.response.AccountLoginResponse;
import com.mogu.apiserver.presentation.authentication.response.RegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/authentication/register")
    public ApiResponse<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.ok(authenticationService.registerUser(request));
    }

    @PostMapping("/authentication/login")
    public ApiResponse<AccountLoginResponse> authenticate(@Valid @RequestBody AccountLoginRequest request) {
        return ApiResponse.ok(authenticationService.login(request));
    }

}
