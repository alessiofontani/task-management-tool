package com.alessiofontani.taskmanagementtool.controller;

import com.alessiofontani.taskmanagementtool.enums.ErrorCode;
import com.alessiofontani.taskmanagementtool.exception.*;
import com.alessiofontani.taskmanagementtool.payload.request.LoginRequest;
import com.alessiofontani.taskmanagementtool.payload.request.RegistrationRequest;
import com.alessiofontani.taskmanagementtool.payload.response.JwtResponse;
import com.alessiofontani.taskmanagementtool.security.jwt.JwtUtil;
import com.alessiofontani.taskmanagementtool.security.jwt.JwtValidationFilter;
import com.alessiofontani.taskmanagementtool.service.AuthService;
import com.alessiofontani.taskmanagementtool.service.UserService;
import com.alessiofontani.taskmanagementtool.util.PasswordUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerApiTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordUtil passwordUtil;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void whenRegisterEndpoint_shouldReturnStatusOK() throws Exception {

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testUsername");
        registrationRequest.setEmail("testUsername@test.com");
        registrationRequest.setPassword("Password123!");
        registrationRequest.setRepeatPassword("Password123!");

        mockMvc.perform(post("/api/auth/register").contentType("application/json")
                .content(objectMapper.writeValueAsString(registrationRequest))).andExpect(status().isOk());
    }

    @Test
    public void whenRegisterEndpoint_withMismatchedPasswords_thenThrowException() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testUsername");
        registrationRequest.setEmail("testUsername@test.com");
        registrationRequest.setPassword("Password123!");
        registrationRequest.setRepeatPassword("Password123!");

        given(userService.registerUser(any(RegistrationRequest.class))).willThrow(new PasswordMismatchException("Password mismatch error"));
        mockMvc.perform(post("/api/auth/register").contentType("application/json")
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Password mismatch error"))
                .andExpect(jsonPath("$.code").value(ErrorCode.PASSWORD_MISMATCH_ERROR.getCode()));
    }

    @Test
    public void whenRegisterEndpoint_withComplexityPasswordError_thenThrowException() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testUsername");
        registrationRequest.setEmail("testUsername@test.com");
        registrationRequest.setPassword("Password123");
        registrationRequest.setRepeatPassword("Password123");


        given(userService.registerUser(any(RegistrationRequest.class))).willThrow(new PasswordComplexityException("Password complexity error"));
        mockMvc.perform(post("/api/auth/register").contentType("application/json")
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Password complexity error"))
                .andExpect(jsonPath("$.code").value(ErrorCode.PASSWORD_COMPLEXITY_ERROR.getCode()));
    }

    @Test
    void whenRegisterUser_withUsernameAlreadyExistsError_thenThrowException() throws Exception {

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("usernameAlreadyExists");
        registrationRequest.setEmail("testUsername@test.com");
        registrationRequest.setPassword("Password123!");
        registrationRequest.setRepeatPassword("Password123!");

        given(userService.registerUser(any(RegistrationRequest.class))).willThrow(new UsernameAlreadyExistsException("Username already exists"));
        MvcResult result = mockMvc.perform(post("/api/auth/register").contentType("application/json")
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Username already exists"))
                .andExpect(jsonPath("$.code").value(ErrorCode.USERNAME_ALREADY_EXISTS.getCode())).andReturn();
    }

    @Test
    void whenRegisterUser_withEmailAlreadyExistsError_thenThrowException() throws Exception {

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testUsername");
        registrationRequest.setEmail("emailAlreadyExists@test.com");
        registrationRequest.setPassword("password123");
        registrationRequest.setRepeatPassword("password123");

        given(userService.registerUser(any(RegistrationRequest.class))).willThrow(new EmailAlreadyExistsException("Email already exists"));
        MvcResult result = mockMvc.perform(post("/api/auth/register").contentType("application/json")
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already exists"))
                .andExpect(jsonPath("$.code").value(ErrorCode.EMAIL_ALREADY_EXISTS.getCode())).andReturn();
    }

    @Test
    void whenAuthenticateEndpoint_thenReturnJwt() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUsername");
        loginRequest.setPassword("password");
        String mockJwt = "mockJwtToken";
        given(authService.authenticateAndRetrieveJwt("testUsername", "password")).willReturn(mockJwt);

        mockMvc.perform(post("/api/auth/login").contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("mockJwtToken"));
    }

    @Test
    void whenAuthenticateUser_withWrongPassword_thenThrowException() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrongpassword");

        given(authService.authenticateAndRetrieveJwt("testuser", "wrongpassword"))
                .willThrow(new InvalidLoginPasswordException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login").contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials"))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_PASSWORD.getCode()));
    }

    @Test
    void whenAuthenticateUser_withNonExistingUsername_thenThrowException() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testNonExistingUser");
        loginRequest.setPassword("wrongpassword");

        given(authService.authenticateAndRetrieveJwt("testNonExistingUser", "wrongpassword"))
                .willThrow(new UserNotFoundException("Username or email not found"));

        mockMvc.perform(post("/api/auth/login").contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Username or email not found"))
                .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.getCode()));
    }


}
