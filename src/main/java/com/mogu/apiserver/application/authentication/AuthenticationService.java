package com.mogu.apiserver.application.authentication;

import com.mogu.apiserver.application.authentication.request.AccountLoginServiceRequest;
import com.mogu.apiserver.application.authentication.request.RegisterServiceRequest;
import com.mogu.apiserver.domain.account.Account;
import com.mogu.apiserver.domain.account.AccountPrincipal;
import com.mogu.apiserver.domain.account.AccountRepository;
import com.mogu.apiserver.domain.account.exception.AccountNotFoundException;
import com.mogu.apiserver.domain.account.exception.NoPermissionException;
import com.mogu.apiserver.domain.authentication.JwtTokenProvider;
import com.mogu.apiserver.domain.authentication.exception.InvalidRefreshTokenException;
import com.mogu.apiserver.domain.user.User;
import com.mogu.apiserver.domain.user.enums.UserStatus;
import com.mogu.apiserver.domain.user.enums.UserType;
import com.mogu.apiserver.domain.user.exception.AlreadyExistEmailException;
import com.mogu.apiserver.domain.user.exception.InactivateUserException;
import com.mogu.apiserver.global.authentication.AuthenticationHelper;
import com.mogu.apiserver.infrastructure.account.AccountJpaRepository;
import com.mogu.apiserver.infrastructure.user.UserJpaRepository;
import com.mogu.apiserver.presentation.authentication.response.AccountLoginResponse;
import com.mogu.apiserver.presentation.authentication.response.RefreshTokenResponse;
import com.mogu.apiserver.presentation.authentication.response.RegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountJpaRepository accountJpaRepository;
    private final AccountRepository accountRepository;

    private final UserJpaRepository userJpaRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponse registerUser(RegisterServiceRequest request) {

        duplicateEmailValidation(request.getEmail());

        User user = User.builder()
                .nickname(request.getNickname())
                .status(UserStatus.ACTIVE)
                .type(UserType.USER)
                .build();


        Account account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();


        account.setUser(user);

        userJpaRepository.save(user);
        accountJpaRepository.save(account);

        AccountPrincipal accountPrincipal = AccountPrincipal.builder()
                .username(account.getEmail())
                .password(account.getPassword())
                .userTypes(account.getUser().getType())
                .build();

        String accessToken = jwtTokenProvider.generateAccessToken(accountPrincipal, jwtTokenProvider.getAccessTokenExpireTime());
        String refreshToken = jwtTokenProvider.generateRefreshToken(accountPrincipal, jwtTokenProvider.getRefreshTokenExpireTime());

        return RegisterResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AccountLoginResponse login(AccountLoginServiceRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Account account = accountRepository.findByEmailWithUser(request.getEmail())
                .orElseThrow(AccountNotFoundException::new);

        if (!account.getUser().isActivated()) {
            throw new InactivateUserException();
        }

        AccountPrincipal accountPrincipal = AccountPrincipal.builder()
                .username(account.getEmail())
                .password(account.getPassword())
                .userTypes(account.getUser().getType())
                .build();


        String accessToken = jwtTokenProvider.generateAccessToken(accountPrincipal, jwtTokenProvider.getAccessTokenExpireTime());
        String refreshToken = jwtTokenProvider.generateRefreshToken(accountPrincipal, jwtTokenProvider.getRefreshTokenExpireTime());

        return AccountLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public RefreshTokenResponse refresh(String token) {
        if (token == null) {
            throw new InvalidRefreshTokenException();
        }

        String email = jwtTokenProvider.extractUsername(token);
        if (email == null) {
            throw new InvalidRefreshTokenException();
        }

        Account account = accountRepository.findByEmailWithUser(email)
                .orElseThrow(AccountNotFoundException::new);

        if (!account.getUser().isActivated()) {
            throw new InactivateUserException();
        }

        AccountPrincipal accountPrincipal = AccountPrincipal.builder()
                .username(account.getEmail())
                .password(account.getPassword())
                .userTypes(account.getUser().getType())
                .build();

        String accessToken = jwtTokenProvider.refreshToken(token, accountPrincipal, jwtTokenProvider.getAccessTokenExpireTime());

        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .build();

    }

    public void verifyIdentity(Long userId) {
        String email = AuthenticationHelper.getEmail();

        String findUserEmail = accountRepository.findByIdAndActiveUser(userId)
                .orElseThrow(() -> new NoPermissionException());

        if (!email.equals(findUserEmail)) {
            throw new NoPermissionException();
        }

    }

    private void duplicateEmailValidation(String email) {
        if (accountJpaRepository.existsByEmailAllIgnoreCase(email)) {
            throw new AlreadyExistEmailException();
        }

    }

}
