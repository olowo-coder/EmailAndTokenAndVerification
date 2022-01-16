package com.example.registrationandemailverify.registration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
