package com.teja.securedapis.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecureApiController {
    @GetMapping(value = "/test")
    public ResponseEntity<String> fetchSecuredData(){
        return ResponseEntity.ok("Some secured data");
    }
}
