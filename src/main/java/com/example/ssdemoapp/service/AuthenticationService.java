package com.example.ssdemoapp.service;

import com.example.ssdemoapp.dto.ApiResponse;
import com.example.ssdemoapp.dto.AuthToken;
import com.example.ssdemoapp.dto.LoginRequest;
import com.example.ssdemoapp.dto.UserDto;

public interface AuthenticationService {
    ApiResponse registerUser(UserDto dto);

    ApiResponse authenticateUser(LoginRequest loginRequest);

    AuthToken verifyOtp(LoginRequest loginRequest);
}
