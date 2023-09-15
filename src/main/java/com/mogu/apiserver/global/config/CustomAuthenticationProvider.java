package com.mogu.apiserver.global.config;

import com.mogu.apiserver.domain.account.Account;
import com.mogu.apiserver.domain.account.exception.AccountNotFoundException;
import com.mogu.apiserver.domain.user.exception.InactivateUserException;
import com.mogu.apiserver.domain.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        Account account = accountRepository.findByEmailWithUser(email)
                .orElseThrow(AccountNotFoundException::new);

        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new AccountNotFoundException();
        }

        if (!account.getUser().isActivated()) {
            throw new InactivateUserException();
        }

        return new UsernamePasswordAuthenticationToken(account, password, new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
