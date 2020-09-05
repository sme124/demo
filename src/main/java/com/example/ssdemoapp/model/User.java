package com.example.ssdemoapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Date;

@Table(name = "users")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private Boolean active;

    private String verificationCode;

    private Date createdDate;

    public boolean isActive() {
        if (active.equals(true)) {
            return active;
        } else {
            return false;
        }
    }
}
