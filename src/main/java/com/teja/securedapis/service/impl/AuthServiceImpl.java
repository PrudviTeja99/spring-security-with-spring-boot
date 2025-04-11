package com.teja.securedapis.service.impl;

import com.teja.securedapis.model.AuthRequest;
import com.teja.securedapis.model.AuthResponse;
import com.teja.securedapis.service.AuthService;
import com.teja.securedapis.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private JwtUtil jwtUtil;

    public AuthServiceImpl(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponse userLogin(AuthRequest authRequest) {
        String token = jwtUtil.generateToken();
        return new AuthResponse(token);
    }
}
