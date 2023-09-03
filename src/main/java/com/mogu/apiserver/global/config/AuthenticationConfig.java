package com.mogu.apiserver.global.config;

import com.mogu.apiserver.domain.account.Account;
import com.mogu.apiserver.domain.account.AccountPrincipal;
import com.mogu.apiserver.domain.user.exception.UserNotFoundException;
import com.mogu.apiserver.infrastructure.account.AccountJpaRepository;
import com.mogu.apiserver.infrastructure.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
                    .orElseThrow(() -> new UserNotFoundException());

            return new AccountPrincipal(account.getEmail(), account.getPassword(), account.getUser().getType());
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
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
