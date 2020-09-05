package com.example.ssdemoapp.service.impl;

import com.example.ssdemoapp.dto.UserDto;
import com.example.ssdemoapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class UserServiceHelper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Method used to save user details into user entity
     *
     * @param dto : request details
     */
    public User saveUserDetails(UserDto dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        if (null != dto.getPassword()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setCreatedDate(new Date());
        user.setActive(false);
        user.setVerificationCode(UUID.randomUUID().toString());
        return user;
    }

    /**
     * Method is used to map mail details
     *
     * @param user    : User entity
     */
    public Map<String, Object> mapMailDetails(User user) {
        final Map<String, Object> usersDetails = new HashMap<>();
        usersDetails.put("name", user.getFirstName() + " " + user.getLastName());
        usersDetails.put("email", user.getEmail());
        usersDetails.put("activationCode", user.getVerificationCode());
        return usersDetails;
    }

    public UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setActive(user.getActive());
        userDto.setEmail(user.getEmail());
        userDto.setCreatedDate(user.getCreatedDate());
        return userDto;
    }
}
