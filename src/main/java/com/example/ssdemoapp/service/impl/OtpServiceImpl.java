package com.example.ssdemoapp.service.impl;

import com.example.ssdemoapp.exception.error.CustomParameterizedException;
import com.example.ssdemoapp.model.OtpDetail;
import com.example.ssdemoapp.model.User;
import com.example.ssdemoapp.repository.OtpDetailRepository;
import com.example.ssdemoapp.service.MailService;
import com.example.ssdemoapp.service.sms.SmsService;
import com.example.ssdemoapp.util.OtpGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class OtpServiceImpl {
    private final ExecutorService pool = Executors.newSingleThreadExecutor();

    @Value("${otp.attempt}")
    private Integer attempt;
    @Value("${otp.validity.time}")
    private String validTime;
    @Value("${otp.simulation")
    private boolean otpSimulation;

    @Autowired
    private MailService mailService;
    @Autowired
    private SmsService smsService;

    @Autowired
    private OtpGeneratorUtil otpGenerator;

    @Autowired
    private OtpDetailRepository otpDetailRepository;

    @Transactional
    public void sendOtp(User user) {
        OtpDetail otpDetail = otpDetailRepository.findByEmail(user.getEmail()).orElseGet(OtpDetail::new);

        String otp = otpGenerator.generateOtp();
        otpDetail.setOtp(otp);
        otpDetail.setEmail(user.getEmail());
        if (attempt > otpDetail.getAttempt()) {
            int attemptCount = otpDetail.getAttempt() + 1;
            otpDetail.setAttempt(attemptCount);
        }
        otpDetail.setMobileNumber(null != user.getMobile() ? user.getVerificationCode() : null);
        OtpDetail updatedDetail = otpDetailRepository.save(otpDetail);
        if (null != updatedDetail.getMobileNumber() && !otpSimulation) {
            validateMobileNumber(user.getMobile());
            smsService.sendOtp(user.getMobile(), otp);
        }
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("name", user.getFirstName() + " " + user.getLastName());
        userDetails.put("email", updatedDetail.getEmail());
        userDetails.put("otp", updatedDetail.getOtp());
        userDetails.put("expiry", validTime);
        pool.submit(() -> mailService.sendOtpVerificationEmail(userDetails));
    }

    private void validateMobileNumber(String mobileNumber) {
        if(mobileNumber.length() != 10) {
            throw new CustomParameterizedException("Mobile number must have 10 digits", HttpStatus.BAD_REQUEST.value());
        }

    }
}
