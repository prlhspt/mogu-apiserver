package com.mogu.apiserver.presentation.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mogu.apiserver.application.authentication.AuthenticationService;
import com.mogu.apiserver.application.authentication.request.AccountLoginServiceRequest;
import com.mogu.apiserver.domain.authentication.JwtTokenProvider;
import com.mogu.apiserver.presentation.authentication.request.AccountLoginRequest;
import com.mogu.apiserver.presentation.authentication.request.RegisterRequest;
import com.mogu.apiserver.presentation.authentication.response.AccountLoginResponse;
import com.mogu.apiserver.presentation.authentication.response.RefreshTokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthenticationController.class)
class AuthenticationControllerTest {


    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;


    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext applicationContext) {
        mockMvc =
                MockMvcBuilders.webAppContextSetup(applicationContext)
                        .build();
    }

    @Test
    @DisplayName("회원가입을 한다.")
    void register() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("test@test.com")
                .password("test1234!@#")
                .nickname("testNickName")
                .build();

        mockMvc.perform(post("/authentication/register")
                        .content(objectMapper.writeValueAsString(registerRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("로그인을 한다.")
    void authenticate() throws Exception {

        AccountLoginRequest accountLoginRequest = AccountLoginRequest.builder()
                .email("test@test.com")
                .password("test1234!@#")
                .build();

        AccountLoginResponse result = AccountLoginResponse
                .builder()
                .accessToken("Bearer testAccessToken")
                .refreshToken("Bearer testRefreshToken")
                .build();

        when(authenticationService.login(any(AccountLoginServiceRequest.class))).thenReturn(result);

        mockMvc.perform(post("/authentication/login")
                        .content(objectMapper.writeValueAsString(accountLoginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists());

    }

    @Test
    @DisplayName("토큰을 갱신한다.")
    void refresh() throws Exception {

        RefreshTokenResponse refreshTokenResponse = RefreshTokenResponse.builder()
                .accessToken("Bearer testAccessToken")
                .build();

        when(authenticationService.refresh(any(String.class))).thenReturn(refreshTokenResponse);

        mockMvc.perform(put("/authentication/refresh")
                        .header("Authorization", "Bearer testToken")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists());
    }

}