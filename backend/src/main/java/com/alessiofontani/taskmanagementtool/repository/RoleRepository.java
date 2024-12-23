package com.alessiofontani.taskmanagementtool.repository;

import com.alessiofontani.taskmanagementtool.model.Role;
import com.alessiofontani.taskmanagementtool.model.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleEnum name);

}