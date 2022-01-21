package com.example.registrationandemailverify.registration;

import com.example.registrationandemailverify.appUser.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

public class Utility {

    public static String getSiteURL(HttpServletRequest request){
        String siteURL = request.getRequestURI().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}