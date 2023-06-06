package com.electronic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.electronic.entites.Role;

public interface RoleRepository extends JpaRepository<Role, String> {

}
