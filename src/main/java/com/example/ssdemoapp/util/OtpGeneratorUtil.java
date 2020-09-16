package com.example.ssdemoapp.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author puday
 */
@Component
public class OtpGeneratorUtil {

    @Value("${otp.length}")
    private Integer otpLength;
    private Random random = new Random();

    public String generateOtp() {
        StringBuilder stringBuilder = new StringBuilder();
        String numbers = "0123456789";
        for (int i = 0; i < otpLength; i++) {
            stringBuilder.append(random.nextInt(numbers.length()));
        }
        return stringBuilder.toString();
    }
}
