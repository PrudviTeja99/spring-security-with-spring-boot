package com.teja.securedapis.util;

import com.teja.securedapis.entity.RefreshTokenEntity;
import com.teja.securedapis.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value(value = "${jwt.secretKey}")
    private String secretKey;
    @Value(value = "${jwt.expiry.accessToken}")
    private long accessTokenExpiry;
    @Value(value = "${jwt.expiry.refreshToken}")
    private long refreshTokenExpiry;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public String generateToken(String username){
        JwtBuilder jwtBuilder = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiry))
                .signWith(getSecretKey());
        return jwtBuilder.compact();
    }

    public String generateRefreshToken(String username){
        JwtBuilder jwtBuilder = Jwts.builder()
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiry))
                .issuedAt(new Date())
                .signWith(getSecretKey());
        return jwtBuilder.compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        return isValidUser(token,userDetails) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token){
        Date now = new Date();
        Date tokenExpiryDate = extractClaims(token,Claims::getExpiration);
        return tokenExpiryDate.before(now);
    }

    public boolean isValidUser(String token,UserDetails userDetails){
        return fetchUsername(token).equals(userDetails.getUsername());
    }

    public boolean isRefreshTokenValid(String refreshToken){
        Optional<RefreshTokenEntity> refreshTokenOptional = refreshTokenRepository.findByTokenAndExpiryAfter(refreshToken, Instant.now());
        return refreshTokenOptional.isPresent();
    }

    public String fetchUsername(String token) {
        return extractClaims(token,Claims::getSubject);
    }

    private <T> T extractClaims(String token,Function<Claims,T> claimResolver){
        Claims claims = Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
        return claimResolver.apply(claims);
    }

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
