package com.mogu.apiserver.presentation.authentication;

import com.mogu.apiserver.application.authentication.AuthenticationService;
import com.mogu.apiserver.global.property.JwtProperty;
import com.mogu.apiserver.global.util.ApiResponseEntity;
import com.mogu.apiserver.presentation.authentication.request.AccountLoginRequest;
import com.mogu.apiserver.presentation.authentication.request.RegisterRequest;
import com.mogu.apiserver.presentation.authentication.response.AccountLoginResponse;
import com.mogu.apiserver.presentation.authentication.response.RefreshTokenResponse;
import com.mogu.apiserver.presentation.authentication.response.RegisterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtProperty jwtProperty;

    @Operation(summary = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "403", description = "회원가입 실패",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
    })
    @PostMapping("/authentication/register")
    public ApiResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request,
                                                        HttpServletResponse response) {
        RegisterResponse registerResponse = authenticationService.registerUser(request.toServiceRequest());
        createCookie(response, registerResponse.getRefreshToken());

        return ApiResponseEntity.ok(registerResponse);
    }

    @Operation(summary = "로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "403", description = "로그인 실패",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
    })
    @PostMapping("/authentication/login")
    public ApiResponseEntity<AccountLoginResponse> authenticate(@Valid @RequestBody AccountLoginRequest request,
                                                                HttpServletResponse response) {

        AccountLoginResponse accountLoginResponse = authenticationService.login(request.toServiceRequest());

        createCookie(response, accountLoginResponse.getRefreshToken());

        return ApiResponseEntity.ok(accountLoginResponse);
    }

    @Operation(summary = "토큰 재발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            @ApiResponse(responseCode = "403", description = "토큰 재발급 실패",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
    })
    @PutMapping("/authentication/refresh")
    public ApiResponseEntity<RefreshTokenResponse> refresh(@CookieValue(name = "refreshToken") String refreshToken) {
        return ApiResponseEntity.ok(authenticationService.refresh(refreshToken));
    }

    private void createCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(jwtProperty.refreshTokenExpireTime.intValue());

        response.addCookie(cookie);
    }

}
