package com.alessiofontani.taskmanagementtool.service;

import com.alessiofontani.taskmanagementtool.model.Role;
import com.alessiofontani.taskmanagementtool.model.RoleEnum;
import com.alessiofontani.taskmanagementtool.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RoleServiceTest {


    @MockBean
    RoleRepository roleRepository;

    @Autowired
    RoleService roleService;

    @BeforeEach
    public void setUp() {
        Role adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setName(RoleEnum.ADMIN);
        when(roleRepository.findByName(RoleEnum.ADMIN)).thenReturn(Optional.of(adminRole));
    }


    @Test
    public void testGetRoleByName_Cache() {
        Role firstCall = roleService.getRoleByName(RoleEnum.ADMIN);
        assertNotNull(firstCall);
        assertEquals(RoleEnum.ADMIN, firstCall.getName());

        Role secondCall = roleService.getRoleByName(RoleEnum.ADMIN);

        assertNotNull(secondCall);
        assertEquals(RoleEnum.ADMIN, secondCall.getName());
        verify(roleRepository, times(1)).findByName(RoleEnum.ADMIN);
    }

}
