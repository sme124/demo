package com.example.ssdemoapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean isActive;
    private Date createdDate;
}
