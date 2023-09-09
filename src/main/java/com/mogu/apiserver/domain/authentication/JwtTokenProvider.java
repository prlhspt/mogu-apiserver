package com.mogu.apiserver.domain.authentication;

import com.mogu.apiserver.domain.authentication.exception.InvalidRefreshTokenException;
import com.mogu.apiserver.global.property.JwtProperty;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final JwtProperty jwtProperty;

    public Long getAccessTokenExpireTime() {
        return jwtProperty.accessTokenExpireTime;
    }

    public Long getRefreshTokenExpireTime() {
        return jwtProperty.refreshTokenExpireTime;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateAccessToken(UserDetails userDetails, Long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("token_type", "access");
        return generateAccessToken(claims, userDetails, expireTime);
    }

    public String generateAccessToken(
            Claims extraClaims,
            UserDetails userDetails,
            Long expireTime
    ) {
        return buildToken(extraClaims, userDetails, expireTime);
    }

    public String generateRefreshToken(
            UserDetails userDetails,
            Long expireTime
    ) {
        Claims claims = Jwts.claims();
        claims.put("token_type", "refresh");
        return buildToken(claims, userDetails, expireTime);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    public String refreshToken(String token, UserDetails userDetails, Long expireTime) {
        if (!isTokenValid(token, userDetails) || !isRefreshToken(token)) {
            throw new InvalidRefreshTokenException();
        }

        Claims claims = extractAllClaims(token);



        System.out.println("claims = " + claims);
        return generateAccessToken(userDetails, expireTime);

    }

    private String buildToken(
            Claims extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperty.secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isRefreshToken(String token) {
        return extractClaim(token, claims -> claims.get("token_type", String.class)).equals("refresh");
    }
}
