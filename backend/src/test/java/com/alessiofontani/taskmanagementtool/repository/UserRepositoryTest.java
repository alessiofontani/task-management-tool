package com.alessiofontani.taskmanagementtool.repository;

import com.alessiofontani.taskmanagementtool.model.Role;
import com.alessiofontani.taskmanagementtool.model.RoleEnum;
import com.alessiofontani.taskmanagementtool.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    private User getMockUser() {
        Role role = new Role();
        role.setId(1L);
        role.setName(RoleEnum.USER);

        User user = new User();
        user.setEmail("test@email.com");
        user.setPassword("hashedPassword");
        user.setUsername("testUsername");
        user.setRoles(List.of(role));
        return user;
    }

    @Test
    public void testFindByUsername() {
        userRepository.save(getMockUser());

        Optional<User> foundUser = userRepository.findByUsername("testUsername");

        assertTrue(foundUser.isPresent());
        assertEquals("testUsername", foundUser.get().getUsername());
    }

    @Test
    public void testFindByEmail() {
        userRepository.save(getMockUser());

        Optional<User> foundUser = userRepository.findByEmail("test@email.com");

        assertTrue(foundUser.isPresent());
        assertEquals("test@email.com", foundUser.get().getEmail());
    }

    @Test
    public void testFindByUsernameOrEmail() {
        userRepository.save(getMockUser());

        Optional<User> foundUserUsername = userRepository.findByUsernameOrEmail("testUsername");
        assertTrue(foundUserUsername.isPresent());
        assertEquals("testUsername", foundUserUsername.get().getUsername());

        Optional<User> foundUserEmail = userRepository.findByUsernameOrEmail("test@email.com");
        assertTrue(foundUserEmail.isPresent());
        assertEquals("test@email.com", foundUserEmail.get().getEmail());

    }

    @Test
    public void testExistsByUsername() {
        userRepository.save(getMockUser());

        boolean exists = userRepository.existsByUsername("testUsername");

        assertTrue(exists);

        boolean existsTwo = userRepository.existsByUsername("wrongUsername");

        assertFalse(existsTwo);
    }


    @Test
    public void testExistsByEmail() {
        userRepository.save(getMockUser());

        boolean exists = userRepository.existsByEmail("test@email.com");

        assertTrue(exists);

        boolean existsTwo = userRepository.existsByEmail("wrong@email.com");

        assertFalse(existsTwo);
    }



}
