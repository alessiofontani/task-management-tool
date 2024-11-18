package com.alessiofontani.taskmanagementtool.security.jwt;

import com.alessiofontani.taskmanagementtool.model.Role;
import com.alessiofontani.taskmanagementtool.model.RoleEnum;
import com.alessiofontani.taskmanagementtool.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {


    JwtUtil jwtUtil;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        jwtUtil = new JwtUtil("Bk/+RwNZOii7VL8EwkY7YH8eH13nPoZ+Qx2kw7jVZJrAPtpEERtQyLRDIEHGFufziKUH4mSqjWcUohCZRVlkJw==");
    }

    @Test
    public void testGenerateToken() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("testUser@email.it");
        Role role = new Role();
        role.setName(RoleEnum.USER);
        user.setRoles(List.of(role));
        String token = jwtUtil.generateJwt(user);
        assertNotNull(token);
    }

    @Test
    public void testValidateTokenSuccess() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("testUser@email.it");
        Role role = new Role();
        role.setName(RoleEnum.USER);
        user.setRoles(List.of(role));
        String token = jwtUtil.generateJwt(user);
        Authentication authentication = jwtUtil.validateJwt(token);
        assertNotNull(authentication);
    }

    @Test
    public void testValidateTokenFail() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("testUser@email.it");
        Role role = new Role();
        role.setName(RoleEnum.USER);
        user.setRoles(List.of(role));
        String token = jwtUtil.generateJwt(user);
        token = token.substring(3) + "asd";
        Authentication authentication = jwtUtil.validateJwt(token);
        assertNull(authentication);
    }

    @Test
    public void testCheckUsername() {
        User user = new User();
        String username = "testUser";
        user.setUsername(username);
        user.setEmail("testUser@email.it");
        Role role = new Role();
        role.setName(RoleEnum.USER);
        user.setRoles(List.of(role));
        String token = jwtUtil.generateJwt(user);
        Authentication authentication = jwtUtil.validateJwt(token);
        String principal = (String) authentication.getPrincipal();
        String roleName = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse(null);
        assertEquals(username, principal);
        assertEquals(roleName, RoleEnum.USER.name());
    }


}
