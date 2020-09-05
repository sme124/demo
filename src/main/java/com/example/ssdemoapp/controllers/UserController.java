package com.example.ssdemoapp.controllers;

import com.example.ssdemoapp.dto.ApiResponse;
import com.example.ssdemoapp.dto.UserDto;
import com.example.ssdemoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{email}/email-verification")
    public ResponseEntity<ApiResponse> verifyEmail(@PathVariable(name = "email") String email, @RequestParam(value = "code") String activationCode) {
        return ResponseEntity.ok().body(userService.verifyEmail(email, activationCode));
    }

    @GetMapping("/active")
    public ResponseEntity<List<UserDto>> getActiveUsers() {
        return ResponseEntity.ok(userService.getActiveUsers());
    }
}
