package com.alessiofontani.taskmanagementtool.repository;

import com.alessiofontani.taskmanagementtool.model.Role;
import com.alessiofontani.taskmanagementtool.model.RoleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testFindByNameExists() {
        Role role = new Role();
        role.setName(RoleEnum.ADMIN);
        roleRepository.save(role);

        Optional<Role> foundRole = roleRepository.findByName(RoleEnum.ADMIN);

        assertTrue(foundRole.isPresent());
        assertEquals(foundRole.get().getName(), RoleEnum.ADMIN);
    }

    @Test
    void testFindByNameNotExists() {
        Optional<Role> foundRole = roleRepository.findByName(RoleEnum.USER);
        assertFalse(foundRole.isPresent());
    }

}
