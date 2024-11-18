package com.alessiofontani.taskmanagementtool.util;

import com.alessiofontani.taskmanagementtool.exception.PasswordComplexityException;
import com.alessiofontani.taskmanagementtool.exception.PasswordMismatchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class PasswordUtilTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private PasswordUtil passwordUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(messageSource.getMessage("password.mismatch", null, Locale.ENGLISH))
                .thenReturn("Passwords do not match.");
        when(messageSource.getMessage("password.complexity", null, Locale.ENGLISH))
                .thenReturn("Password does not meet complexity requirements.");
    }

    @Test
    public void testHashPassword() {
        String password = "testPasswordHashed";
        String hashedPassword = passwordUtil.hashPassword(password);
        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
    }

    @Test
    public void testRawPasswordMatchesHashedPasswordOk() {
        String password = "testPasswordHashed";
        String hashedPassword = passwordUtil.hashPassword(password);
        assertTrue(passwordUtil.doesRawPasswordMatchesHashedPassword(password, hashedPassword));
    }

    @Test
    public void testRawPasswordMatchesHashedPasswordKo() {
        String password = "testPasswordHashed";
        String hashedPassword = passwordUtil.hashPassword(password);
        assertFalse(passwordUtil.doesRawPasswordMatchesHashedPassword(password + "w", hashedPassword));
    }

    @Test
    public void testValidatePasswordOk() {
        String password = "testPasswordHashed";
        String hashedPassword = passwordUtil.hashPassword(password);
        assertFalse(passwordUtil.doesRawPasswordMatchesHashedPassword(password + "w", hashedPassword));
    }

    @Test
    public void testValidatePassword_withPasswordMismatchException() {
        String password = "TestPassword1234!";
        String repeatPassword = "TestPassword1234!!";
        PasswordMismatchException exception = assertThrows(PasswordMismatchException.class, () -> passwordUtil.validatePassword(password, repeatPassword));
        assertEquals(exception.getMessage(), "Passwords do not match.");
    }

    @Test
    public void testValidatePassword_withPasswordComplexityException() {
        String password = "testpassword";
        String repeatPassword = "testpassword";
        PasswordComplexityException exception = assertThrows(PasswordComplexityException.class, () -> passwordUtil.validatePassword(password, repeatPassword));
        assertEquals(exception.getMessage(), "Password does not meet complexity requirements.");
    }


}
