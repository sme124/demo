package com.example.ssdemoapp.service.impl;

import com.example.ssdemoapp.dto.ApiResponse;
import com.example.ssdemoapp.dto.AuthToken;
import com.example.ssdemoapp.dto.LoginRequest;
import com.example.ssdemoapp.dto.UserDto;
import com.example.ssdemoapp.exception.error.CustomParameterizedException;
import com.example.ssdemoapp.model.User;
import com.example.ssdemoapp.repository.UserRepository;
import com.example.ssdemoapp.security.TokenProvider;
import com.example.ssdemoapp.service.AuthenticationService;
import com.example.ssdemoapp.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.ssdemoapp.util.Constants.*;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    // Send email
    private final ExecutorService pool = Executors.newSingleThreadExecutor();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceHelper helper;

    @Autowired
    private MailService mailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    @Override
    @Transactional
    public ApiResponse registerUser(UserDto dto) {
        final Optional<User> optUser = userRepository.findByEmail(dto.getEmail());

        if (optUser.isPresent()) {
            throw new CustomParameterizedException(USER_EXISTS, HttpStatus.BAD_REQUEST.value());
        }

        User user = helper.saveUserDetails(dto);
        pool.submit(() -> mailService.sendVerificationEmail(helper.mapMailDetails(user)));
        userRepository.save(user);
        return new ApiResponse(true, EMAIL_REGISTERED);    }

    @Override
    public AuthToken authenticateUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new CustomParameterizedException(USER_NOT_FOUND , HttpStatus.NOT_FOUND.value()));
        if (!user.isActive()) {
            throw new CustomParameterizedException("User is inactive, Kindly verify your email first", HttpStatus.BAD_REQUEST.value());
        }

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new AuthToken(tokenProvider.generateToken(authentication));
    }
}
