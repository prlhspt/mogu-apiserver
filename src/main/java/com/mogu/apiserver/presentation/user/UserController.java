package com.mogu.apiserver.presentation.user;

import com.mogu.apiserver.application.authentication.AuthenticationService;
import com.mogu.apiserver.application.user.UserService;
import com.mogu.apiserver.global.util.ApiResponseEntity;
import com.mogu.apiserver.presentation.user.request.UpdateUserRequest;
import com.mogu.apiserver.presentation.user.response.CheckUserResponse;
import com.mogu.apiserver.presentation.user.response.DeleteUserResponse;
import com.mogu.apiserver.presentation.user.response.GetUserInfoResponse;
import com.mogu.apiserver.presentation.user.response.UpdateUserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @GetMapping("/test")
    public void test() throws Exception {
        throw new Exception("error");
    }

    @Operation(summary = "회원 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
    })
    @GetMapping("/users/{userId}")
    public ApiResponseEntity<GetUserInfoResponse> getUserInfo(@PathVariable Long userId) {
        authenticationService.verifyIdentity(userId);

        return ApiResponseEntity.ok(userService.getUserDetails(userId));
    }

    @Operation(summary = "이메일로 중복 체크")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 중복 체크 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
    })
    @GetMapping("/users/check-register")
    public ApiResponseEntity<CheckUserResponse> checkRegister(@RequestParam String email) {
        return ApiResponseEntity.ok(userService.checkEmailRegistrationStatus(email));
    }

    @Operation(summary = "회원 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
    })
    @PatchMapping("/users/{userId}")
    public ApiResponseEntity<UpdateUserResponse> updateUser(
            @PathVariable Long userId,
            @RequestBody @Valid UpdateUserRequest request
    ) {
        authenticationService.verifyIdentity(userId);

        return ApiResponseEntity.ok(userService.updateUser(userId, request.toServiceRequest()));
    }

    @Operation(summary = "회원 탈퇴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = {@Content(schema = @Schema(implementation = ApiResponseEntity.class))}),
    })
    @DeleteMapping("/users/{userId}")
    public ApiResponseEntity<DeleteUserResponse> deleteUser(@PathVariable Long userId) {
        authenticationService.verifyIdentity(userId);

        return ApiResponseEntity.ok(userService.deleteUserById(userId));
    }

}
