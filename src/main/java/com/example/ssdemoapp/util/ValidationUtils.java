package com.example.ssdemoapp.util;

import com.example.ssdemoapp.exception.error.CustomParameterizedException;
import org.springframework.http.HttpStatus;

public class ValidationUtils {

    public static void validateMobileOrEmail(String mobile) {
        if (mobile.trim().isEmpty()) {
            throw new CustomParameterizedException("Mobile number could not be empty", HttpStatus.BAD_REQUEST.value());
        }
    }
}
