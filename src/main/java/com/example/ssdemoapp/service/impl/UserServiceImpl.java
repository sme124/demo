package com.example.ssdemoapp.service.impl;

import com.example.ssdemoapp.dto.ApiResponse;
import com.example.ssdemoapp.dto.UserDto;
import com.example.ssdemoapp.exception.error.CustomParameterizedException;
import com.example.ssdemoapp.model.User;
import com.example.ssdemoapp.repository.UserRepository;
import com.example.ssdemoapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.ssdemoapp.util.Constants.USER_NOT_FOUND;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceHelper helper;

    @Override
    @Transactional
    public ApiResponse verifyEmail(String email, String activationCode) {

        log.info("Verify user's email : {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomParameterizedException(USER_NOT_FOUND , HttpStatus.NOT_FOUND.value()));

        if (null == user.getVerificationCode() && user.isActive()) {
            throw new CustomParameterizedException("Your account is already activated !", HttpStatus.BAD_REQUEST.value());
        }
        if (!activationCode.equals(user.getVerificationCode())) {
            throw new CustomParameterizedException("Sorry, your email could not verify", HttpStatus.BAD_REQUEST.value());
        }
        user.setActive(true);
        user.setVerificationCode(null);
        userRepository.save(user);
        return new ApiResponse(true, "User email is verified successfully !");
    }

    @Override
    public List<UserDto> getActiveUsers() {

        log.info("Get active users list...");

        List<User> users = userRepository.findAllByActiveEqualsOrderByCreatedDateDesc(true);
        if(users.isEmpty()) {
            throw new CustomParameterizedException("No active user can be found", HttpStatus.BAD_REQUEST.value());
        }

        return users.stream().map(helper::mapToUserDto).collect(Collectors.toList());
    }
}
