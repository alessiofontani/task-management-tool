package com.alessiofontani.taskmanagementtool.service;

import com.alessiofontani.taskmanagementtool.exception.InvalidLoginPasswordException;
import com.alessiofontani.taskmanagementtool.model.User;
import com.alessiofontani.taskmanagementtool.security.jwt.JwtUtil;
import com.alessiofontani.taskmanagementtool.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    JwtUtil jwtUtil;

    @Mock
    UserService userService;

    @Mock
    MessageSource messageSource;

    @Mock
    PasswordUtil passwordUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void authenticateAndRetrieveJwt_thenReturnJwt() {
        String username = "testUser";
        String password = "testPassword";
        String hashedPassword = "hashedPassword";
        String expectedJwt = "tokenTest";

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(hashedPassword);
        given(userService.getUserByUsernameOrEmail(username)).willReturn(Optional.of(mockUser));
        given(passwordUtil.doesRawPasswordMatchesHashedPassword(password, mockUser.getPassword())).willReturn(true);
        given(jwtUtil.generateJwt(mockUser)).willReturn(expectedJwt);

        String actualJwt = authService.authenticateAndRetrieveJwt(username, password);
        assertEquals(actualJwt, expectedJwt);
    }

    @Test
    public void authenticateAndRetrieveJwt_whenUserNotFound_thenThrowUserNotFoundException() {
        String username = "testUser";
        String password = "testPassword";

        given(userService.getUserByUsernameOrEmail(username)).willThrow(new UsernameNotFoundException("User not found"));
        verify(userService, atMostOnce()).getUserByUsernameOrEmail(username);
        verify(passwordUtil, never()).doesRawPasswordMatchesHashedPassword(any(), any());
        verify(jwtUtil, never()).generateJwt(any());
        assertDoesNotThrow(() -> new InvalidLoginPasswordException(any()));
        assertThrows(UsernameNotFoundException.class, () -> authService.authenticateAndRetrieveJwt(username, password), "User not found");
    }

    @Test
    public void authenticateAndRetrieveJwt_whenPasswordIsNotValid_thenThrowInvalidLoginException() {
        String username = "testUser";
        String password = "testPassword";
        String hashedPassword = "hashedPassword";
        String expectedJwt = "tokenTest";

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(hashedPassword);
        given(userService.getUserByUsernameOrEmail(username)).willReturn(Optional.of(mockUser));
        given(messageSource.getMessage("password.invalid",null, Locale.ENGLISH)).willReturn("Password is not correct");
        given(passwordUtil.doesRawPasswordMatchesHashedPassword(password, mockUser.getPassword())).willReturn(false);
        verify(jwtUtil, never()).generateJwt(mockUser);
        verify(userService, atMostOnce()).getUserByUsernameOrEmail(username);
        verify(messageSource, atMostOnce()).getMessage("password.invalid",null, Locale.ENGLISH);
        verify(passwordUtil, atMostOnce()).doesRawPasswordMatchesHashedPassword(password, mockUser.getPassword());
        assertThrows(InvalidLoginPasswordException.class, () -> authService.authenticateAndRetrieveJwt(username, password), "Password is not correct");
    }

}
