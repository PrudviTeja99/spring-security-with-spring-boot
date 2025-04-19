package com.teja.securedapis.service;

import com.teja.securedapis.model.AuthRefreshTokenRequest;
import com.teja.securedapis.model.AuthRequest;
import com.teja.securedapis.model.AuthResponse;

public interface AuthService {
    AuthResponse userLogin(AuthRequest authRequest);

    AuthResponse userRegiser(AuthRequest authRequest);

    AuthResponse userRefresh(AuthRefreshTokenRequest authRefreshTokenRequest);
}
