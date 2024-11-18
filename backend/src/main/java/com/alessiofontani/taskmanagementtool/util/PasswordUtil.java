package com.alessiofontani.taskmanagementtool.util;

import com.alessiofontani.taskmanagementtool.exception.PasswordComplexityException;
import com.alessiofontani.taskmanagementtool.exception.PasswordMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.regex.Pattern;

@Component
public class PasswordUtil {

    private final MessageSource messageSource;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";

    public PasswordUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean doesRawPasswordMatchesHashedPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    public void validatePassword(String password, String repeatPassword) {
        if(!password.equals(repeatPassword)) {
            throw new PasswordMismatchException(messageSource.getMessage("password.mismatch",null, Locale.ENGLISH));
        }
        if(!password.matches(PASSWORD_PATTERN)) {
            throw new PasswordComplexityException(messageSource.getMessage("password.complexity",null, Locale.ENGLISH));
        }
    }

}
