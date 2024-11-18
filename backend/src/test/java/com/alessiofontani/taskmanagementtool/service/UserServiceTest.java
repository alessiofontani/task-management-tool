package com.alessiofontani.taskmanagementtool.service;

import com.alessiofontani.taskmanagementtool.exception.EmailAlreadyExistsException;
import com.alessiofontani.taskmanagementtool.exception.UsernameAlreadyExistsException;
import com.alessiofontani.taskmanagementtool.model.Role;
import com.alessiofontani.taskmanagementtool.model.RoleEnum;
import com.alessiofontani.taskmanagementtool.model.User;
import com.alessiofontani.taskmanagementtool.payload.request.RegistrationRequest;
import com.alessiofontani.taskmanagementtool.repository.UserRepository;
import com.alessiofontani.taskmanagementtool.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RoleService roleService;

    @Mock
    MessageSource messageSource;

    @Mock
    PasswordUtil passwordUtil;

    @InjectMocks
    UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User createMockUser() {
        Role role = new Role();
        role.setId(1L);
        role.setName(RoleEnum.USER);

        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("test@mail.com");
        user.setPassword("hashedPassword");
        user.setRoles(List.of(role));
        return user;
    }

    @Test
    void testGetUserById() {
        User mockUser = createMockUser();
        given(userRepository.findById(1L)).willReturn(Optional.of(mockUser));

        Long id = 1L;
        User foundUser = userService.getUserById(id);

        assertNotNull(foundUser);
        assertEquals("testUser", foundUser.getUsername());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void testGetUserById_UserNotFound() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        Long id = 1L;

        User foundUser = userService.getUserById(id);

        assertNull(foundUser);
        verify(userRepository, times(1)).findById(id);
    }


    @Test
    void testGetUserByUsername() {
        User mockUser = createMockUser();
        given(userRepository.findByUsername("testUser")).willReturn(Optional.of(mockUser));

        User foundUser = userService.getUserByUsername("testUser");

        assertNotNull(foundUser);
        assertEquals("testUser", foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void testGetUserByUsername_UserNotFound() {
        given(userRepository.findByUsername("nonexistentUser")).willReturn(Optional.empty());

        User foundUser = userService.getUserByUsername("nonexistentUser");

        assertNull(foundUser);
        verify(userRepository, times(1)).findByUsername("nonexistentUser");
    }


    @Test
    void testGetUserByEmail() {
        User mockUser = createMockUser();
        given(userRepository.findByEmail("test@mail.com")).willReturn(Optional.of(mockUser));

        User foundUser = userService.getUserByEmail("test@mail.com");

        assertNotNull(foundUser);
        assertEquals("test@mail.com", foundUser.getEmail());
        verify(userRepository, times(1)).findByEmail("test@mail.com");
    }

    @Test
    void testGetUserByEmail_UserNotFound() {
        given(userRepository.findByEmail("nonexistent@mail.com")).willReturn(Optional.empty());

        User foundUser = userService.getUserByEmail("nonexistent@mail.com");

        assertNull(foundUser);
        verify(userRepository, times(1)).findByEmail("nonexistent@mail.com");
    }


    @Test
    void testUserExistsByUsername() {
        given(userRepository.existsByUsername("testUser")).willReturn(true);

        boolean exists = userService.userExistsByUsername("testUser");

        assertTrue(exists);
        verify(userRepository, times(1)).existsByUsername("testUser");
    }



    @Test
    void testUserExistsByEmail() {
        given(userRepository.existsByEmail("test@mail.com")).willReturn(true);

        boolean exists = userService.userExistsByEmail("test@mail.com");

        assertTrue(exists);
        verify(userRepository, times(1)).existsByEmail("test@mail.com");
    }

    @Test
    public void testRegisterUser_thenReturnUser() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail("test@mail.com");
        registrationRequest.setPassword("PasswordTest123!");
        registrationRequest.setRepeatPassword("PasswordTest123!");
        registrationRequest.setUsername("testUsername");

        String hashedPassword = "hashedPassword";

        Role role = new Role();
        role.setName(RoleEnum.USER);
        role.setId(1L);

        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(hashedPassword);
        user.setRoles(List.of(role));

        given(userRepository.existsByUsername(registrationRequest.getUsername())).willReturn(false);
        given(userRepository.existsByEmail(registrationRequest.getEmail())).willReturn(false);
        given(roleService.getRoleByName(RoleEnum.USER)).willReturn(role);
        given(passwordUtil.hashPassword(registrationRequest.getPassword())).willReturn(hashedPassword);
        given(userRepository.save(any())).willReturn(user);

        User registeredUser = userService.registerUser(registrationRequest);


        assertNotNull(registeredUser);
        assertEquals(user.getUsername(), registeredUser.getUsername());
        assertEquals(user.getEmail(), registeredUser.getEmail());
        assertEquals(user.getPassword(), registeredUser.getPassword());
        assertEquals(user.getRoles(), registeredUser.getRoles());


        verify(userRepository, times(1)).existsByUsername(registrationRequest.getUsername());
        verify(userRepository, times(1)).existsByEmail(registrationRequest.getEmail());
        verify(userRepository, times(1)).save(any());
        verify(roleService, times(1)).getRoleByName(RoleEnum.USER);
        verify(passwordUtil, times(1)).hashPassword(registrationRequest.getPassword());

    }



    @Test
    public void testRegisterUser_whenUserExistsByUsername_thenThrowNewUsernameAlreadyExistsException() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail("test@mail.com");
        registrationRequest.setPassword("PasswordTest123!");
        registrationRequest.setRepeatPassword("PasswordTest123!");
        registrationRequest.setUsername("testUsername");
        String hashedPassword = "hashedPassword";
        Role role = new Role();
        role.setName(RoleEnum.USER);
        role.setId(1L);
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(hashedPassword);
        user.setRoles(List.of(role));
        given(messageSource.getMessage("username.already.exists", new String[]{registrationRequest.getUsername()}, Locale.ENGLISH)).willReturn("Username already exists");
        given(userRepository.existsByUsername(registrationRequest.getUsername())).willReturn(true);
        assertThrows(UsernameAlreadyExistsException.class, () -> userService.registerUser(registrationRequest), "Username already exists");
        verify(userRepository, times(1)).existsByUsername(registrationRequest.getUsername());
        verify(userRepository, never()).existsByEmail(registrationRequest.getEmail());
        verify(userRepository, never()).save(any());
        verify(roleService, never()).getRoleByName(RoleEnum.USER);
        verify(passwordUtil, never()).hashPassword(registrationRequest.getPassword());

    }

    @Test
    public void testRegisterUser_whenUserExistsByEmail_thenThrowNewEmailAlreadyExistsException() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail("test@mail.com");
        registrationRequest.setPassword("PasswordTest123!");
        registrationRequest.setRepeatPassword("PasswordTest123!");
        registrationRequest.setUsername("testUsername");
        String hashedPassword = "hashedPassword";
        Role role = new Role();
        role.setName(RoleEnum.USER);
        role.setId(1L);
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(hashedPassword);
        user.setRoles(List.of(role));
        given(messageSource.getMessage("email.already.exists", new String[]{registrationRequest.getEmail()}, Locale.ENGLISH)).willReturn("Email already exists");
        given(userRepository.existsByUsername(registrationRequest.getUsername())).willReturn(false);
        given(userRepository.existsByEmail(registrationRequest.getEmail())).willReturn(true);
        assertThrows(EmailAlreadyExistsException.class, () -> userService.registerUser(registrationRequest), "Email already exists");
        verify(userRepository, times(1)).existsByUsername(registrationRequest.getUsername());
        verify(userRepository, times(1)).existsByEmail(registrationRequest.getEmail());
        verify(userRepository, never()).save(any());
        verify(roleService, never()).getRoleByName(RoleEnum.USER);
        verify(passwordUtil, never()).hashPassword(registrationRequest.getPassword());
    }

}
