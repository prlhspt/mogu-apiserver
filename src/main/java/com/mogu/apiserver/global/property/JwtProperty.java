package com.mogu.apiserver.global.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProperty {

    @Value("${security.jwt.secret-key}")
    public String secretKey;

    @Value("${security.jwt.access-token.expiration}")
    public Long accessTokenExpireTime;

    @Value("${security.jwt.refresh-token.expiration}")
    public Long refreshTokenExpireTime;

}
