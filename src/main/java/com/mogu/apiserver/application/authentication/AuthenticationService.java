package com.mogu.apiserver.application.authentication;

import com.mogu.apiserver.domain.account.Account;
import com.mogu.apiserver.domain.account.AccountPrincipal;
import com.mogu.apiserver.domain.authentication.JwtTokenProvider;
import com.mogu.apiserver.domain.user.User;
import com.mogu.apiserver.domain.user.enums.UserStatus;
import com.mogu.apiserver.domain.user.enums.UserType;
import com.mogu.apiserver.domain.user.exception.AlreadyExistLoginIdException;
import com.mogu.apiserver.domain.user.exception.UserNotFoundException;
import com.mogu.apiserver.infrastructure.account.AccountJpaRepository;
import com.mogu.apiserver.infrastructure.account.AccountRepository;
import com.mogu.apiserver.infrastructure.user.UserRepository;
import com.mogu.apiserver.presentation.authentication.request.AccountLoginRequest;
import com.mogu.apiserver.presentation.authentication.request.RegisterRequest;
import com.mogu.apiserver.presentation.authentication.response.AccountLoginResponse;
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

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponse registerUser(RegisterRequest request) {

        duplicateEmailValidation(request.getEmail());

        User user = User.builder()
                .nickname(request.getNickname())
                .status(UserStatus.ACTIVE)
                .type(UserType.USER)
                .build();

        userRepository.save(user);

        Account account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();


        account.setUser(user);

        accountJpaRepository.save(account);

        AccountPrincipal accountPrincipal = AccountPrincipal.builder()
                .username(request.getEmail())
                .password(request.getPassword())
                .userTypes(user.getType())
                .build();

        String accessToken = jwtTokenProvider.generateToken(accountPrincipal);
        String refreshToken = jwtTokenProvider.generateRefreshToken(accountPrincipal);

        return RegisterResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public AccountLoginResponse login(AccountLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Account account = accountRepository.findByEmailWithUser(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException());

        AccountPrincipal accountPrincipal = AccountPrincipal.builder()
                .username(request.getEmail())
                .password(request.getPassword())
                .userTypes(account.getUser().getType())
                .build();

        String accessToken = jwtTokenProvider.generateToken(accountPrincipal);
        String refreshToken = jwtTokenProvider.generateRefreshToken(accountPrincipal);

        return AccountLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void duplicateEmailValidation(String email) {
        if (accountJpaRepository.existsByEmailAllIgnoreCase(email)) {
            throw new AlreadyExistLoginIdException();
        }

    }

}
