package com.mogu.apiserver.application.authentication;

import com.mogu.apiserver.application.authentication.request.AccountLoginServiceRequest;
import com.mogu.apiserver.application.authentication.request.RegisterServiceRequest;
import com.mogu.apiserver.domain.account.Account;
import com.mogu.apiserver.domain.account.exception.AccountNotFoundException;
import com.mogu.apiserver.domain.user.User;
import com.mogu.apiserver.domain.user.enums.UserStatus;
import com.mogu.apiserver.domain.user.enums.UserType;
import com.mogu.apiserver.domain.user.exception.AlreadyExistEmailException;
import com.mogu.apiserver.domain.user.exception.InactivateUserException;
import com.mogu.apiserver.infrastructure.account.AccountJpaRepository;
import com.mogu.apiserver.infrastructure.user.UserJpaRepository;
import com.mogu.apiserver.presentation.authentication.response.AccountLoginResponse;
import com.mogu.apiserver.presentation.authentication.response.RegisterResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입으로 계정을 생성한다.")
    void registerUser() {
        RegisterServiceRequest request = RegisterServiceRequest.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("test1234"))
                .nickname("test")
                .build();

        RegisterResponse registerResponse = authenticationService.registerUser(request);

        assertThat(registerResponse).isNotNull();
        assertThat(registerResponse)
                .extracting("accessToken", "refreshToken")
                .allSatisfy(field -> assertThat(field).isNotNull());
    }

    @Test
    @DisplayName("회원가입시 이메일이 중복되는 경우 예외를 발생시킨다.")
    void registerUserWithDuplicateEmail() {
        RegisterServiceRequest request = RegisterServiceRequest.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("test1234"))
                .nickname("test")
                .build();

        authenticationService.registerUser(request);

        assertThatThrownBy(() -> authenticationService.registerUser(request))
                .isInstanceOf(AlreadyExistEmailException.class)
                .hasMessage("이미 존재하는 이메일입니다.");
    }

    @Test
    @DisplayName("로그인으로 토큰을 발급받는다.")
    void login() {
        String plainTextPassword = "test1234";

        Account account = Account.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode(plainTextPassword))
                .build();

        User user = User.builder()
                .nickname("test")
                .status(UserStatus.ACTIVE)
                .type(UserType.USER)
                .build();

        account.setUser(user);

        userJpaRepository.save(user);
        accountJpaRepository.save(account);

        AccountLoginServiceRequest accountLoginServiceRequest = AccountLoginServiceRequest.builder()
                .email(account.getEmail())
                .password(plainTextPassword)
                .build();

        AccountLoginResponse accountLoginResponse = authenticationService.login(accountLoginServiceRequest);

        assertThat(accountLoginResponse).isNotNull();
        assertThat(accountLoginResponse)
                .extracting("accessToken", "refreshToken")
                .allSatisfy(field -> assertThat(field).isNotNull());
    }

    @Test
    @DisplayName("로그인시 비밀번호가 틀린 경우 예외를 발생시킨다.")
    void loginWithWrongPassword() {
        String plainTextPassword = "test1234";

        Account account = Account.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode(plainTextPassword))
                .build();

        User user = User.builder()
                .nickname("test")
                .status(UserStatus.ACTIVE)
                .type(UserType.USER)
                .build();

        account.setUser(user);

        userJpaRepository.save(user);
        accountJpaRepository.save(account);

        AccountLoginServiceRequest accountLoginServiceRequest = AccountLoginServiceRequest.builder()
                .email(account.getEmail())
                .password("wrongPassword")
                .build();

        assertThatThrownBy(() -> authenticationService.login(accountLoginServiceRequest))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessage("계정을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("로그인시 비활성화된 유저인 경우 예외를 발생시킨다.")
    void loginWithInactivateUser() {
        String plainTextPassword = "test1234";

        Account account = Account.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode(plainTextPassword))
                .build();

        User user = User.builder()
                .nickname("test")
                .status(UserStatus.INACTIVE)
                .type(UserType.USER)
                .build();

        account.setUser(user);

        userJpaRepository.save(user);
        accountJpaRepository.save(account);

        AccountLoginServiceRequest accountLoginServiceRequest = AccountLoginServiceRequest.builder()
                .email(account.getEmail())
                .password(plainTextPassword)
                .build();

        assertThatThrownBy(() -> authenticationService.login(accountLoginServiceRequest))
                .isInstanceOf(InactivateUserException.class)
                .hasMessage("비활성화된 유저입니다.");
    }

}
