package com.teja.securedapis.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecureApiController {
    @GetMapping(value = "/test")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> fetchSecuredData(){
        return ResponseEntity.ok("Some user secured data");
    }
    @GetMapping(value = "/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> fetchAdminData()
    {
        return ResponseEntity.ok("Some admin string data");
    }
}
