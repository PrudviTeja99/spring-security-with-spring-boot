package com.teja.securedapis.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value(value = "${jwt.secretKey}")
    private String secretKey;
    public String generateToken(){
        JwtBuilder jwtBuilder = Jwts.builder()
                .subject("username")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1800000))
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
