package com.example.bankomat.controller;


import com.example.bankomat.dto.LoginDto;

import com.example.bankomat.jwt.JwtProwider;
import com.example.bankomat.response.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProwider jwtProwider;

    //Login qilib kirish
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            String token = jwtProwider.generateToken(loginDto.getUsername());
            return ResponseEntity.status(201).body(token);
        } catch (Exception c) {
            return ResponseEntity.status(403).body(new ApiResponse("Login yoki parol xato", false));
        }
    }


}
