package com.alessiofontani.taskmanagementtool.service;


import com.alessiofontani.taskmanagementtool.exception.EmailAlreadyExistsException;
import com.alessiofontani.taskmanagementtool.exception.UsernameAlreadyExistsException;
import com.alessiofontani.taskmanagementtool.model.Role;
import com.alessiofontani.taskmanagementtool.model.RoleEnum;
import com.alessiofontani.taskmanagementtool.model.User;
import com.alessiofontani.taskmanagementtool.payload.request.RegistrationRequest;
import com.alessiofontani.taskmanagementtool.repository.UserRepository;

import com.alessiofontani.taskmanagementtool.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleService roleService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    PasswordUtil passwordUtil;

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public Optional<User> getUserByUsernameOrEmail(String username) {
        return userRepository.findByUsernameOrEmail(username);
    }

    public Boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User registerUser(RegistrationRequest registrationRequest) {
        String username = registrationRequest.getUsername();
        String email = registrationRequest.getEmail();
        if(userExistsByUsername(username)) {
            throw new UsernameAlreadyExistsException(messageSource.getMessage("username.already.exists", new String[]{username}, Locale.ENGLISH));
        }
        if(userExistsByEmail(email)) {
            throw new EmailAlreadyExistsException(messageSource.getMessage("email.already.exists", new String[]{email}, Locale.ENGLISH));
        }
        Role role = roleService.getRoleByName(RoleEnum.USER);
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordUtil.hashPassword(registrationRequest.getPassword()));
        user.setRoles(List.of(role));
        return userRepository.save(user);
    }

}
