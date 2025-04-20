package com.teja.securedapis.service.impl;

import com.teja.securedapis.entity.UserEntity;
import com.teja.securedapis.model.AuthRefreshTokenRequest;
import com.teja.securedapis.model.AuthRequest;
import com.teja.securedapis.model.AuthResponse;
import com.teja.securedapis.service.AuthService;
import com.teja.securedapis.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserServiceImpl userDetailsService, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse userLogin(AuthRequest authRequest) {
        //TODO Handle unauthorized exception
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        String token = jwtUtil.generateToken(userDetails.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());
        jwtUtil.createUserSessionInDatabase(refreshToken);
        return new AuthResponse(token,refreshToken);
    }

    @Override
    public AuthResponse userRegiser(AuthRequest authRequest) {
        //TODO Handle unauthorized exception
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        userEntity.setUsername(authRequest.getUsername());
        userDetailsService.saveUserDetails(userEntity);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        String token = jwtUtil.generateToken(userDetails.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());
        //Save user refresh token in database as a user session
        jwtUtil.createUserSessionInDatabase(refreshToken);
        return new AuthResponse(token,refreshToken);
    }

    @Override
    public AuthResponse userRefresh(AuthRefreshTokenRequest authRefreshTokenRequest){

        boolean isRefreshTokenValid = jwtUtil.isRefreshTokenValid(authRefreshTokenRequest.getRefreshToken());
        if(isRefreshTokenValid){
            String username = jwtUtil.fetchUsername(authRefreshTokenRequest.getRefreshToken());
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            boolean isValidUser = jwtUtil.isValidUser(authRefreshTokenRequest.getRefreshToken(), userDetails);
            if(isValidUser){
                jwtUtil.invalidateRefreshToken(authRefreshTokenRequest.getRefreshToken());
                String token = jwtUtil.generateToken(userDetails.getUsername());
                String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());
                //Add user refresh token in database a user session
                jwtUtil.createUserSessionInDatabase(refreshToken);
                return new AuthResponse(token,refreshToken);
            }
            else{
                //TODO Created proper unauthorized exception
                throw new RuntimeException("Username not found");
            }
        }
        else{
            //TODO Created proper unauthorized exception
            throw new RuntimeException("Unauthorized Exception");
        }
    }
}
