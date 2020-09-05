package com.example.ssdemoapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AuthToken {

    private String accessToken;
    private String tokenType = "Bearer";

    public AuthToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
