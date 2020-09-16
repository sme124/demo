package com.example.ssdemoapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "otp_dtail")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class OtpDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 15)
    private String mobileNumber;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false, length = 10)
    private String otp;
    @Column(nullable = false)
    private LocalDateTime otpCreatedOn = LocalDateTime.now();
    @Column
    private LocalDateTime otpBlockedOn;
    @Column(nullable = false)
    private Boolean verified = false;
    @Column
    private Integer attempt = 0;
    @Column
    private Boolean disable = false;

}
