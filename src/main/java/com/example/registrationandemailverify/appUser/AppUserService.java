package com.example.registrationandemailverify.appUser;

import com.example.registrationandemailverify.registration.token.ConfirmationToken;
import com.example.registrationandemailverify.registration.token.ConfirmationTokenRepository;
import com.example.registrationandemailverify.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final static String USER_NOT_FOUND = "User with email %s not found";
    private final ConfirmationTokenRepository confirmationTokenRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }

    public String  signUpUser(AppUser appUser){
       boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();
       if(userExists){
           throw  new IllegalStateException("email taken");
       }

       String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
       appUser.setPassword(encodedPassword);
       appUserRepository.save(appUser);
       String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), appUser);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        // TODO: Send email
        return token;
    }

    public void enableUser(String email){
        appUserRepository.findByEmail(email).ifPresent((user) ->
               user.setEnabled(true));
    }

    public List<AppUser> getAllAppUsers() {
        return appUserRepository.findAll();
    }

    @Transactional
    public String deleteAppUser(Long id) {
        confirmationTokenRepository.deleteByAppUserId(id);
        appUserRepository.deleteById(id);
        return "deleted";
    }
}
