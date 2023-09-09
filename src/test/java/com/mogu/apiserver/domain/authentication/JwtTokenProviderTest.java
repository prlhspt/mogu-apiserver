package com.mogu.apiserver.domain.authentication;

import com.mogu.apiserver.domain.account.AccountPrincipal;
import com.mogu.apiserver.domain.authentication.exception.InvalidRefreshTokenException;
import com.mogu.apiserver.domain.user.enums.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("AccessToken을 생성한다.")
    public void generateAccessToken() {
        AccountPrincipal accountPrincipal = AccountPrincipal.builder()
                .username("test@test.com")
                .password("test1234")
                .userTypes(UserType.USER)
                .build();

        String result = jwtTokenProvider.generateAccessToken(accountPrincipal, jwtTokenProvider.getAccessTokenExpireTime());

        System.out.println("result = " + result);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("RefreshToken을 생성한다.")
    public void generateRefreshToken() {
        AccountPrincipal accountPrincipal = AccountPrincipal.builder()
                .username("test@test.com")
                .password("test1234")
                .userTypes(UserType.USER)
                .build();

        String result = jwtTokenProvider.generateRefreshToken(accountPrincipal, jwtTokenProvider.getRefreshTokenExpireTime());

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("RefreshToken을 사용해서 AccessToken을 재발급한다.")
    public void refreshToken() {
        AccountPrincipal accountPrincipal = AccountPrincipal.builder()
                .username("test@test.com")
                .password("test1234")
                .userTypes(UserType.USER)
                .build();

        String refreshToken = jwtTokenProvider.generateRefreshToken(accountPrincipal, jwtTokenProvider.getRefreshTokenExpireTime());

        String result = jwtTokenProvider.refreshToken(refreshToken, accountPrincipal, jwtTokenProvider.getAccessTokenExpireTime());

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("유효한 AccessToken으로 유효성을 검사하면 true를 반환한다.")
    public void isTokenValidTrue() {
        AccountPrincipal accountPrincipal = AccountPrincipal.builder()
                .username("test@test.com")
                .password("test1234")
                .userTypes(UserType.USER)
                .build();

        String result = jwtTokenProvider.generateAccessToken(accountPrincipal, jwtTokenProvider.getAccessTokenExpireTime());

        boolean tokenValid = jwtTokenProvider.isTokenValid(result, accountPrincipal);
        assertThat(tokenValid).isTrue();

    }

    @Test
    @DisplayName("만료된 AccessToken으로 유효성을 검사하면 false를 반환한다.")
    public void isTokenValidFalse() {
        AccountPrincipal accountPrincipal = AccountPrincipal.builder()
                .username("test@test.com")
                .password("test1234")
                .userTypes(UserType.USER)
                .build();

        Long expiredTime = -1L;
        String result = jwtTokenProvider.generateAccessToken(accountPrincipal, expiredTime);
        boolean tokenValid = jwtTokenProvider.isTokenValid(result, accountPrincipal);

        assertThat(tokenValid).isFalse();
    }

    @Test
    @DisplayName("만료된 RefreshToken으로 토큰 재발급 요청을 하면 InvalidRefreshTokenException이 발생한다.")
    public void refreshInvalidRefreshTokenException() {
        AccountPrincipal accountPrincipal = AccountPrincipal.builder()
                .username("test@test.com")
                .password("test1234")
                .userTypes(UserType.USER)
                .build();

        Long expiredTime = -1L;
        String token = jwtTokenProvider.generateRefreshToken(accountPrincipal, expiredTime);

        assertThatThrownBy(() -> jwtTokenProvider.refreshToken(token, accountPrincipal, jwtTokenProvider.getAccessTokenExpireTime()))
                .isInstanceOf(InvalidRefreshTokenException.class)
                .hasMessage("유효하지 않은 리프레쉬 토큰입니다.");
    }
}