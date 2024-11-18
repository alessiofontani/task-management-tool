package com.alessiofontani.taskmanagementtool.controller;

import com.alessiofontani.taskmanagementtool.exception.*;
import com.alessiofontani.taskmanagementtool.payload.request.LoginRequest;
import com.alessiofontani.taskmanagementtool.payload.request.RegistrationRequest;
import com.alessiofontani.taskmanagementtool.payload.response.JwtResponse;
import com.alessiofontani.taskmanagementtool.service.AuthService;
import com.alessiofontani.taskmanagementtool.service.UserService;
import com.alessiofontani.taskmanagementtool.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private PasswordUtil passwordUtil;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test case for registration
    @Test
    void whenRegisterUser_thenReturnStatusOK() {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testUsername");
        registrationRequest.setEmail("testUsername@test.com");
        registrationRequest.setPassword("Password123!");
        registrationRequest.setRepeatPassword("Password123!");

        ResponseEntity<?> response = authController.registerUser(registrationRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(passwordUtil, times(1)).validatePassword(registrationRequest.getPassword(), registrationRequest.getRepeatPassword());
        verify(userService, times(1)).registerUser(registrationRequest);
    }

    @Test
    void whenRegisterUser_withMismatchedPasswords_thenThrowException() {

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testUsername");
        registrationRequest.setEmail("testUsername@test.com");
        registrationRequest.setPassword("Password123!");
        registrationRequest.setRepeatPassword("Password456");

        doThrow(new PasswordMismatchException("Passwords do not match"))
                .when(passwordUtil).validatePassword(anyString(), anyString());

        Exception exception = assertThrows(PasswordMismatchException.class, () -> {
            authController.registerUser(registrationRequest);
        });
        assertEquals("Passwords do not match", exception.getMessage());
        verify(userService, never()).registerUser(any());
    }

    @Test
    void whenRegisterUser_withComplexityPasswordError_thenThrowException() {

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testUsername");
        registrationRequest.setEmail("testUsername@test.com");
        registrationRequest.setPassword("password123");
        registrationRequest.setRepeatPassword("password123");

        doThrow(new PasswordComplexityException("Passwords complexity doesn't meet requirements"))
                .when(passwordUtil).validatePassword(anyString(), anyString());

        Exception exception = assertThrows(PasswordComplexityException.class, () -> {
            authController.registerUser(registrationRequest);
        });
        assertEquals("Passwords complexity doesn't meet requirements", exception.getMessage());
        verify(passwordUtil,times(1)).validatePassword("password123", "password123");
        verify(userService, never()).registerUser(any());
    }

    @Test
    void whenRegisterUser_withUsernameAlreadyExistsError_thenThrowException() {

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("usernameAlreadyExists");
        registrationRequest.setEmail("testUsername@test.com");
        registrationRequest.setPassword("Password123!");
        registrationRequest.setRepeatPassword("Password123!");

        doThrow(new UsernameAlreadyExistsException("Username already exists"))
                .when(userService).registerUser(registrationRequest);

        Exception exception = assertThrows(UsernameAlreadyExistsException.class, () -> {
            authController.registerUser(registrationRequest);
        });
        assertEquals("Username already exists", exception.getMessage());
        verify(userService, times(1)).registerUser(registrationRequest);
        verify(passwordUtil, times(1)).validatePassword("Password123!", "Password123!");
    }

    @Test
    void whenRegisterUser_withEmailAlreadyExistsError_thenThrowException() {

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testUsername");
        registrationRequest.setEmail("emailAlreadyExists@test.com");
        registrationRequest.setPassword("ano");
        registrationRequest.setRepeatPassword("Password123!");

        doThrow(new EmailAlreadyExistsException("Email already exists"))
                .when(userService).registerUser(registrationRequest);

        Exception exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            authController.registerUser(registrationRequest);
        });
        assertEquals("Email already exists", exception.getMessage());
        verify(userService, times(1)).registerUser(registrationRequest);
        verify(passwordUtil, times(1)).validatePassword("ano", "Password123!");
    }


    @Test
    void whenAuthenticateUser_thenReturnJwtResponse() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUsername");
        loginRequest.setPassword("password");

        String mockJwt = "mockJwtToken";
        when(authService.authenticateAndRetrieveJwt(anyString(), anyString())).thenReturn(mockJwt);

        ResponseEntity<JwtResponse> response = authController.authenticateUser(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockJwt, response.getBody().getJwt());
        verify(authService, times(1)).authenticateAndRetrieveJwt(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @Test
    void whenAuthenticateUser_withWrongPassword_thenThrowException() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrongpassword");

        when(authService.authenticateAndRetrieveJwt(anyString(), anyString()))
                .thenThrow(new InvalidLoginPasswordException("Invalid credentials"));

        Exception exception = assertThrows(InvalidLoginPasswordException.class, () -> {
            authController.authenticateUser(loginRequest);
        });

        assertEquals("Invalid credentials", exception.getMessage());
        verify(authService, times(1)).authenticateAndRetrieveJwt(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @Test
    void whenAuthenticateUser_withNonExistingUsername_thenThrowException() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testNonExistingUser");
        loginRequest.setPassword("wrongpassword");

        when(authService.authenticateAndRetrieveJwt(anyString(), anyString()))
                .thenThrow(new UserNotFoundException("Username or email not found"));

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            authController.authenticateUser(loginRequest);
        });

        assertEquals("Username or email not found", exception.getMessage());
        verify(authService, times(1)).authenticateAndRetrieveJwt(loginRequest.getUsername(), loginRequest.getPassword());
    }

}
