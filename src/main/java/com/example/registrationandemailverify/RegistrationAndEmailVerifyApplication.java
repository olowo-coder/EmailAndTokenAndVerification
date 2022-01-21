package com.example.registrationandemailverify;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootApplication
@OpenAPIDefinition
public class RegistrationAndEmailVerifyApplication {


    public static void main(String[] args) {
        SpringApplication.run(RegistrationAndEmailVerifyApplication.class, args);
    }

}
