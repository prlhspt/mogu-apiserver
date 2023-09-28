package com.mogu.apiserver.presentation.user;

import com.mogu.apiserver.application.authentication.AuthenticationService;
import com.mogu.apiserver.application.user.UserService;
import com.mogu.apiserver.global.util.ApiResponseEntity;
import com.mogu.apiserver.presentation.user.request.UpdateUserRequest;
import com.mogu.apiserver.presentation.user.response.CheckUserResponse;
import com.mogu.apiserver.presentation.user.response.DeleteUserResponse;
import com.mogu.apiserver.presentation.user.response.GetUserInfoResponse;
import com.mogu.apiserver.presentation.user.response.UpdateUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @GetMapping("/users/{userId}")
    public ApiResponseEntity<GetUserInfoResponse> getUserInfo(@PathVariable Long userId) {
        authenticationService.verifyIdentity(userId);

        return ApiResponseEntity.ok(userService.getUserDetails(userId));
    }

    @GetMapping("/users/check-register")
    public ApiResponseEntity<CheckUserResponse> checkRegister(@RequestParam String email) {
        return ApiResponseEntity.ok(userService.checkEmailRegistrationStatus(email));
    }

    @PatchMapping("/users/{userId}")
    public ApiResponseEntity<UpdateUserResponse> updateUser(
            @PathVariable Long userId,
            @RequestBody UpdateUserRequest request
    ) {
        authenticationService.verifyIdentity(userId);

        return ApiResponseEntity.ok(userService.updateUser(userId, request));
    }

    @DeleteMapping("/users/{userId}")
    public ApiResponseEntity<DeleteUserResponse> deleteUser(@PathVariable Long userId) {
        authenticationService.verifyIdentity(userId);

        return ApiResponseEntity.ok(userService.deleteUserById(userId));
    }

}
