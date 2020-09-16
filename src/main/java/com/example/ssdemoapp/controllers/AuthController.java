package com.example.ssdemoapp.controllers;

import com.example.ssdemoapp.dto.ApiResponse;
import com.example.ssdemoapp.dto.AuthToken;
import com.example.ssdemoapp.dto.LoginRequest;
import com.example.ssdemoapp.dto.UserDto;
import com.example.ssdemoapp.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody UserDto dto) {
        return ResponseEntity.ok(authService.registerUser(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticate(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }

    @PostMapping("/otp-verification")
    public ResponseEntity<AuthToken> validateOtp(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.verifyOtp(loginRequest));
    }

}
