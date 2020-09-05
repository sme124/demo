package com.example.ssdemoapp.service;

import com.example.ssdemoapp.dto.ApiResponse;
import com.example.ssdemoapp.dto.UserDto;

import java.util.List;

public interface UserService {

    ApiResponse verifyEmail(String email, String verificationCode);

    List<UserDto> getActiveUsers();
}
