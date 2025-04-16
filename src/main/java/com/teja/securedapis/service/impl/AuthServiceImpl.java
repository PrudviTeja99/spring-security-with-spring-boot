package com.teja.securedapis.service.impl;

import com.teja.securedapis.entity.UserEntity;
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
    private JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;
    private UserServiceImpl userDetailsService;
    private PasswordEncoder passwordEncoder;

    public AuthServiceImpl(JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserServiceImpl userDetailsService, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse userLogin(AuthRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        String token = jwtUtil.generateToken(userDetails.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());
        return new AuthResponse(token,refreshToken);
    }

    @Override
    public AuthResponse userRegiser(AuthRequest authRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        userEntity.setUsername(authRequest.getUsername());
        userDetailsService.saveUserDetails(userEntity);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        String token = jwtUtil.generateToken(userDetails.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());
        return new AuthResponse(token,refreshToken);
    }

    @Override
    public AuthResponse userRefresh(AuthRequest authRequest){
        return null;
    }
}
