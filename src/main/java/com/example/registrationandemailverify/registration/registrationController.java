package com.example.registrationandemailverify.registration;

import com.example.registrationandemailverify.appUser.AppUser;
import com.example.registrationandemailverify.appUser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/registration")
@AllArgsConstructor
public class registrationController {
    private final RegistrationService registrationService;
    private final AppUserService appUserService;

    @PostMapping
    public String register(@RequestBody RegistrationRequest request, HttpServletRequest site){
        return registrationService.register(request, site);
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token){
        return registrationService.confirmToken(token);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AppUser> allUsers(){
        return appUserService.getAllAppUsers();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppUser(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(appUserService.deleteAppUser(id));
    }
}
