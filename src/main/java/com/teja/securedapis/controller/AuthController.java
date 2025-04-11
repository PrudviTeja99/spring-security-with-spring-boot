package com.teja.securedapis.controller;

import com.teja.securedapis.model.AuthRequest;
import com.teja.securedapis.model.AuthResponse;
import com.teja.securedapis.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> userLogin(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok(authService.userLogin(authRequest));
    }
    @PostMapping(value = "/register")
    public ResponseEntity<AuthResponse> userRegister(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok(authService.userRegiser(authRequest));
    }
}
