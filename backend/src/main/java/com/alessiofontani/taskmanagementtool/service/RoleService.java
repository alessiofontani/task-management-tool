package com.alessiofontani.taskmanagementtool.service;

import com.alessiofontani.taskmanagementtool.model.Role;
import com.alessiofontani.taskmanagementtool.model.RoleEnum;
import com.alessiofontani.taskmanagementtool.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Cacheable(value = "roles", key = "#role.name()",  unless = "#result == null")
    public Role getRoleByName(RoleEnum role) {
        return roleRepository.findByName(role).orElse(null);
    }

}
