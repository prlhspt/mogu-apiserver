package com.mogu.apiserver.application.user;

import com.mogu.apiserver.application.user.request.UpdateUserServiceRequest;
import com.mogu.apiserver.domain.account.Account;
import com.mogu.apiserver.domain.user.User;
import com.mogu.apiserver.domain.user.enums.UserStatus;
import com.mogu.apiserver.domain.user.enums.UserType;
import com.mogu.apiserver.domain.user.exception.InactivateUserException;
import com.mogu.apiserver.domain.user.exception.UserNotFoundException;
import com.mogu.apiserver.infrastructure.account.AccountJpaRepository;
import com.mogu.apiserver.infrastructure.user.UserJpaRepository;
import com.mogu.apiserver.presentation.user.response.CheckUserResponse;
import com.mogu.apiserver.presentation.user.response.GetUserInfoResponse;
import com.mogu.apiserver.presentation.user.response.UpdateUserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Service
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    AccountJpaRepository accountJpaRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("사용자 정보를 조회한다.")
    void getUserDetails() {
        User user = User.builder()
                .nickname("test")
                .status(UserStatus.ACTIVE)
                .type(UserType.USER)
                .build();

        userJpaRepository.save(user);

        GetUserInfoResponse getUserInfoResponse = userService.getUserDetails(user.getId());

        assertThat(getUserInfoResponse).isNotNull();
        assertThat(getUserInfoResponse.getNickname()).isEqualTo(user.getNickname());
    }

    @Test
    @DisplayName("사용자가 활성화되어있는지 확인한다.")
    void checkEmailRegistrationStatus() {
        Account account = Account.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("test123"))
                .build();

        User user = User.builder()
                .nickname("test")
                .status(UserStatus.ACTIVE)
                .type(UserType.USER)
                .build();

        account.setUser(user);

        userJpaRepository.save(user);
        accountJpaRepository.save(account);

        CheckUserResponse checkUserResponse = userService.checkEmailRegistrationStatus(account.getEmail());

        assertThat(checkUserResponse).isNotNull();
        assertThat(checkUserResponse.getIsRegistered()).isEqualTo(true);
        assertThat(checkUserResponse.getUserStatus()).isEqualTo(user.getStatus());

    }

    @Test
    @DisplayName("사용자가 존재하지 않는지 확인한다.")
    void checkEmailRegistrationNotExistStatus() {
        String invalidEmail = "invalid@test.com";

        Account account = Account.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("test123"))
                .build();

        User user = User.builder()
                .nickname("test")
                .status(UserStatus.ACTIVE)
                .type(UserType.USER)
                .build();

        account.setUser(user);

        userJpaRepository.save(user);
        accountJpaRepository.save(account);

        CheckUserResponse checkUserResponse = userService.checkEmailRegistrationStatus(invalidEmail);

        assertThat(checkUserResponse).isNotNull();
        assertThat(checkUserResponse.getIsRegistered()).isEqualTo(false);
        assertThat(checkUserResponse.getUserStatus()).isNull();
    }

    @Test
    @DisplayName("사용자가 비활성화되어있는지 확인한다.")
    void checkEmailRegistrationInactiveStatus() {
        Account account = Account.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("test123"))
                .build();

        User user = User.builder()
                .nickname("test")
                .status(UserStatus.INACTIVE)
                .type(UserType.USER)
                .build();

        account.setUser(user);

        userJpaRepository.save(user);
        accountJpaRepository.save(account);

        CheckUserResponse checkUserResponse = userService.checkEmailRegistrationStatus(account.getEmail());

        assertThat(checkUserResponse).isNotNull();
        assertThat(checkUserResponse.getIsRegistered()).isEqualTo(true);
        assertThat(checkUserResponse.getUserStatus()).isEqualTo(user.getStatus());
    }

    @Test
    @DisplayName("사용자 정보를 수정한다.")
    void updateUser() {
        User user = User.builder()
                .nickname("test")
                .status(UserStatus.ACTIVE)
                .type(UserType.USER)
                .build();

        userJpaRepository.save(user);

        UpdateUserServiceRequest updateUserRequest = UpdateUserServiceRequest.builder()
                .nickname("test2")
                .build();

        UpdateUserResponse updateUserResponse = userService.updateUser(user.getId(), updateUserRequest);

        User result = userJpaRepository.findById(updateUserResponse.getUserId())
                .orElseThrow(UserNotFoundException::new);

        assertThat(result).isNotNull();
        assertThat(result.getNickname()).isEqualTo(updateUserRequest.getNickname());
    }

    @Test
    @DisplayName("사용자를 삭제한다.")
    void deleteUserById() {
        User user = User.builder()
                .nickname("test")
                .status(UserStatus.ACTIVE)
                .type(UserType.USER)
                .build();

        userJpaRepository.save(user);

        userService.deleteUserById(user.getId());

        User result = userJpaRepository.findById(user.getId())
                .orElseThrow(UserNotFoundException::new);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.INACTIVE);
    }

    @Test
    @DisplayName("이미 비활성화 된 사용자를 삭제하려고 할 때 예외가 발생한다.")
    void deleteUserByIdAlreadyInactiveUser() {
        User user = User.builder()
                .nickname("test")
                .status(UserStatus.INACTIVE)
                .type(UserType.USER)
                .build();

        userJpaRepository.save(user);

        assertThatThrownBy(() -> userService.deleteUserById(user.getId()))
                .isInstanceOf(InactivateUserException.class);
    }

}