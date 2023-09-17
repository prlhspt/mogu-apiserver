package com.mogu.apiserver.global.config;

import com.mogu.apiserver.domain.account.Account;
import com.mogu.apiserver.domain.account.AccountPrincipal;
import com.mogu.apiserver.domain.account.AccountRepository;
import com.mogu.apiserver.domain.account.exception.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final AccountRepository accountRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Account account = accountRepository.findByEmailWithUser(username)
                    .orElseThrow(AccountNotFoundException::new);

            return new AccountPrincipal(account.getEmail(), account.getPassword(), account.getUser().getType());
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
