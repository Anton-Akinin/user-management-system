package com.example.usermanagementsystem.repository;

import com.example.usermanagementsystem.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
