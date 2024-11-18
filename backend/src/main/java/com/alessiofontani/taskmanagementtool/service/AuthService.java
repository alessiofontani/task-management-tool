package com.alessiofontani.taskmanagementtool.service;

import com.alessiofontani.taskmanagementtool.exception.InvalidLoginPasswordException;
import com.alessiofontani.taskmanagementtool.exception.UserNotFoundException;
import com.alessiofontani.taskmanagementtool.model.User;
import com.alessiofontani.taskmanagementtool.security.jwt.JwtUtil;
import com.alessiofontani.taskmanagementtool.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@Slf4j
public class AuthService {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserService userService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    PasswordUtil passwordUtil;

    public String authenticateAndRetrieveJwt(String username, String password) {
        User user = userService.getUserByUsernameOrEmail(username).orElseThrow(() -> new UserNotFoundException(messageSource.getMessage("user.not.found",null, Locale.ENGLISH)));
        boolean isPasswordValid = passwordUtil.doesRawPasswordMatchesHashedPassword(password, user.getPassword());
        if(!isPasswordValid) {
            throw new InvalidLoginPasswordException(messageSource.getMessage("password.invalid",null, Locale.ENGLISH));
        }
        return jwtUtil.generateJwt(user);
    }

}
