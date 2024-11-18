package com.alessiofontani.taskmanagementtool.controller;

import com.alessiofontani.taskmanagementtool.payload.request.LoginRequest;
import com.alessiofontani.taskmanagementtool.payload.request.RegistrationRequest;
import com.alessiofontani.taskmanagementtool.payload.response.JwtResponse;
import com.alessiofontani.taskmanagementtool.service.AuthService;
import com.alessiofontani.taskmanagementtool.service.UserService;
import com.alessiofontani.taskmanagementtool.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @Autowired
    PasswordUtil passwordUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        passwordUtil.validatePassword(registrationRequest.getPassword(), registrationRequest.getRepeatPassword());
        userService.registerUser(registrationRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        String jwt = authService.authenticateAndRetrieveJwt(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

}
