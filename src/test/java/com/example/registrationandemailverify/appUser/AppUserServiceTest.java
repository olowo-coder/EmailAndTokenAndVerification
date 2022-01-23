package com.example.registrationandemailverify.appUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.registrationandemailverify.registration.token.ConfirmationTokenRepository;
import com.example.registrationandemailverify.registration.token.ConfirmationTokenService;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AppUserService.class, BCryptPasswordEncoder.class})
@ExtendWith(SpringExtension.class)
class AppUserServiceTest {
    @MockBean
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserService appUserService;

    @MockBean
    private ConfirmationTokenRepository confirmationTokenRepository;

    @MockBean
    private ConfirmationTokenService confirmationTokenService;

    @Test
    void testLoadUserByUsername() throws UsernameNotFoundException {
        AppUser appUser = new AppUser();
        appUser.setAppUserRole(AppUserRole.USER);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setId(123L);
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("iloveyou");
        Optional<AppUser> ofResult = Optional.of(appUser);
        when(this.appUserRepository.findByEmail((String) any())).thenReturn(ofResult);
        assertSame(appUser, this.appUserService.loadUserByUsername("jane.doe@example.org"));
        verify(this.appUserRepository).findByEmail((String) any());
        assertTrue(this.appUserService.getAllAppUsers().isEmpty());
    }

    @Test
    void testLoadUserByUsername2() throws UsernameNotFoundException {
        when(this.appUserRepository.findByEmail((String) any())).thenThrow(new IllegalStateException("foo"));
        assertThrows(IllegalStateException.class, () -> this.appUserService.loadUserByUsername("jane.doe@example.org"));
        verify(this.appUserRepository).findByEmail((String) any());
    }

    @Test
    void testLoadUserByUsername3() throws UsernameNotFoundException {
        when(this.appUserRepository.findByEmail((String) any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> this.appUserService.loadUserByUsername("jane.doe@example.org"));
        verify(this.appUserRepository).findByEmail((String) any());
    }

    @Test
    void testSignUpUser() {
        AppUser appUser = new AppUser();
        appUser.setAppUserRole(AppUserRole.USER);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setId(123L);
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("iloveyou");
        Optional<AppUser> ofResult = Optional.of(appUser);
        when(this.appUserRepository.findByEmail((String) any())).thenReturn(ofResult);

        AppUser appUser1 = new AppUser();
        appUser1.setAppUserRole(AppUserRole.USER);
        appUser1.setEmail("jane.doe@example.org");
        appUser1.setEnabled(true);
        appUser1.setFirstName("Jane");
        appUser1.setId(123L);
        appUser1.setLastName("Doe");
        appUser1.setLocked(true);
        appUser1.setPassword("iloveyou");
        assertThrows(IllegalStateException.class, () -> this.appUserService.signUpUser(appUser1));
        verify(this.appUserRepository).findByEmail((String) any());
    }

    @Test
    void testSignUpUser2() {
        when(this.appUserRepository.findByEmail((String) any())).thenThrow(new IllegalStateException("foo"));

        AppUser appUser = new AppUser();
        appUser.setAppUserRole(AppUserRole.USER);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setId(123L);
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("iloveyou");
        assertThrows(IllegalStateException.class, () -> this.appUserService.signUpUser(appUser));
        verify(this.appUserRepository).findByEmail((String) any());
    }

    @Test
    void testSignUpUser3() {
        doNothing().when(this.confirmationTokenService)
                .saveConfirmationToken((com.example.registrationandemailverify.registration.token.ConfirmationToken) any());

        AppUser appUser = new AppUser();
        appUser.setAppUserRole(AppUserRole.USER);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setId(123L);
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("iloveyou");
        when(this.appUserRepository.save((AppUser) any())).thenReturn(appUser);
        when(this.appUserRepository.findByEmail((String) any())).thenReturn(Optional.empty());

        AppUser appUser1 = new AppUser();
        appUser1.setAppUserRole(AppUserRole.USER);
        appUser1.setEmail("jane.doe@example.org");
        appUser1.setEnabled(true);
        appUser1.setFirstName("Jane");
        appUser1.setId(123L);
        appUser1.setLastName("Doe");
        appUser1.setLocked(true);
        appUser1.setPassword("iloveyou");
        this.appUserService.signUpUser(appUser1);
        verify(this.confirmationTokenService)
                .saveConfirmationToken((com.example.registrationandemailverify.registration.token.ConfirmationToken) any());
        verify(this.appUserRepository).findByEmail((String) any());
        verify(this.appUserRepository).save((AppUser) any());
        assertTrue(this.appUserService.getAllAppUsers().isEmpty());
    }

    @Test
    void testSignUpUser4() {
        doThrow(new IllegalStateException("foo")).when(this.confirmationTokenService)
                .saveConfirmationToken((com.example.registrationandemailverify.registration.token.ConfirmationToken) any());

        AppUser appUser = new AppUser();
        appUser.setAppUserRole(AppUserRole.USER);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setId(123L);
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("iloveyou");
        when(this.appUserRepository.save((AppUser) any())).thenReturn(appUser);
        when(this.appUserRepository.findByEmail((String) any())).thenReturn(Optional.empty());

        AppUser appUser1 = new AppUser();
        appUser1.setAppUserRole(AppUserRole.USER);
        appUser1.setEmail("jane.doe@example.org");
        appUser1.setEnabled(true);
        appUser1.setFirstName("Jane");
        appUser1.setId(123L);
        appUser1.setLastName("Doe");
        appUser1.setLocked(true);
        appUser1.setPassword("iloveyou");
        assertThrows(IllegalStateException.class, () -> this.appUserService.signUpUser(appUser1));
        verify(this.confirmationTokenService)
                .saveConfirmationToken((com.example.registrationandemailverify.registration.token.ConfirmationToken) any());
        verify(this.appUserRepository).findByEmail((String) any());
        verify(this.appUserRepository).save((AppUser) any());
    }

    @Test
    void testEnableUser() {
        AppUser appUser = new AppUser();
        appUser.setAppUserRole(AppUserRole.USER);
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setId(123L);
        appUser.setLastName("Doe");
        appUser.setLocked(true);
        appUser.setPassword("iloveyou");
        Optional<AppUser> ofResult = Optional.of(appUser);
        when(this.appUserRepository.findByEmail((String) any())).thenReturn(ofResult);
        this.appUserService.enableUser("jane.doe@example.org");
        verify(this.appUserRepository).findByEmail((String) any());
        assertTrue(this.appUserService.getAllAppUsers().isEmpty());
    }

    @Test
    void testEnableUser2() {
        when(this.appUserRepository.findByEmail((String) any())).thenThrow(new IllegalStateException("foo"));
        assertThrows(IllegalStateException.class, () -> this.appUserService.enableUser("jane.doe@example.org"));
        verify(this.appUserRepository).findByEmail((String) any());
    }

    @Test
    void testEnableUser3() {
        when(this.appUserRepository.findByEmail((String) any())).thenReturn(Optional.empty());
        this.appUserService.enableUser("jane.doe@example.org");
        verify(this.appUserRepository).findByEmail((String) any());
        assertTrue(this.appUserService.getAllAppUsers().isEmpty());
    }

    @Test
    void testGetAllAppUsers() {
        ArrayList<AppUser> appUserList = new ArrayList<>();
        when(this.appUserRepository.findAll()).thenReturn(appUserList);
        List<AppUser> actualAllAppUsers = this.appUserService.getAllAppUsers();
        assertSame(appUserList, actualAllAppUsers);
        assertTrue(actualAllAppUsers.isEmpty());
        verify(this.appUserRepository).findAll();
    }

    @Test
    void testGetAllAppUsers2() {
        when(this.appUserRepository.findAll()).thenThrow(new IllegalStateException("foo"));
        assertThrows(IllegalStateException.class, () -> this.appUserService.getAllAppUsers());
        verify(this.appUserRepository).findAll();
    }

    @Test
    void testDeleteAppUser() {
        doNothing().when(this.confirmationTokenRepository).deleteByAppUserId((Long) any());
        doNothing().when(this.appUserRepository).deleteById((Long) any());
        assertEquals("deleted", this.appUserService.deleteAppUser(123L));
        verify(this.confirmationTokenRepository).deleteByAppUserId((Long) any());
        verify(this.appUserRepository).deleteById((Long) any());
        assertTrue(this.appUserService.getAllAppUsers().isEmpty());
    }

    @Test
    void testDeleteAppUser2() {
        doNothing().when(this.confirmationTokenRepository).deleteByAppUserId((Long) any());
        doThrow(new IllegalStateException("foo")).when(this.appUserRepository).deleteById((Long) any());
        assertThrows(IllegalStateException.class, () -> this.appUserService.deleteAppUser(123L));
        verify(this.confirmationTokenRepository).deleteByAppUserId((Long) any());
        verify(this.appUserRepository).deleteById((Long) any());
    }

    @Test
    void testDeleteAppUser3() {
        doThrow(new IllegalStateException("foo")).when(this.confirmationTokenRepository).deleteByAppUserId((Long) any());
        assertThrows(IllegalStateException.class, () -> this.appUserService.deleteAppUser(123L));
        verify(this.confirmationTokenRepository).deleteByAppUserId((Long) any());
    }
}

