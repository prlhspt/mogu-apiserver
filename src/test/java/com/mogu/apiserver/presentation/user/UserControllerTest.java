package com.mogu.apiserver.presentation.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mogu.apiserver.application.authentication.AuthenticationService;
import com.mogu.apiserver.application.user.UserService;
import com.mogu.apiserver.application.user.request.UpdateUserServiceRequest;
import com.mogu.apiserver.domain.authentication.JwtTokenProvider;
import com.mogu.apiserver.presentation.user.request.UpdateUserRequest;
import com.mogu.apiserver.presentation.user.response.CheckUserResponse;
import com.mogu.apiserver.presentation.user.response.DeleteUserResponse;
import com.mogu.apiserver.presentation.user.response.GetUserInfoResponse;
import com.mogu.apiserver.presentation.user.response.UpdateUserResponse;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
    }

    @Test
    @DisplayName("회원 정보를 조회한다.")
    void getUserInfo() throws Exception {
        GetUserInfoResponse getUserInfoResponse = GetUserInfoResponse.builder()
                .nickname("testNickname")
                .build();

        when(userService.getUserDetails(any(Long.class))).thenReturn(getUserInfoResponse);

        mockMvc.perform(get("/users/{userId}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nickname").exists());
    }

    @Test
    @DisplayName("이메일 중복 체크를 한다.")
    void checkRegister() throws Exception {
        CheckUserResponse checkUserResponse = CheckUserResponse.builder()
                .isRegistered(false)
                .build();

        when(userService.checkEmailRegistrationStatus(any(String.class))).thenReturn(checkUserResponse);

        mockMvc.perform(get("/users/check-register")
                        .param("email", "test@test.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isRegistered").exists());

    }

    @Test
    @DisplayName("회원 정보를 수정한다.")
    void updateUser() throws Exception {
        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .nickname("testNickname")
                .build();

        UpdateUserResponse updateUserResponse = UpdateUserResponse.builder()
                .userId(1L)
                .build();

        when(userService.updateUser(any(Long.class), any(UpdateUserServiceRequest.class))).thenReturn(updateUserResponse);

        mockMvc.perform(patch("/users/{userId}", 1L)
                        .content(objectMapper.writeValueAsString(updateUserRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 탈퇴를 한다.")
    void deleteUser() throws Exception {
        DeleteUserResponse deleteUserResponse = DeleteUserResponse.builder()
                .userId(1L)
                .build();

        when(userService.deleteUserById(any(Long.class))).thenReturn(deleteUserResponse);

        mockMvc.perform(delete("/users/{userId}", 1L))
                .andDo(print())
                .andExpect(status().isOk());
    }

}