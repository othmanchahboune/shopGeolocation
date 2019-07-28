package com.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.entities.auth.Role;

public interface RoleRepository extends MongoRepository<Role, String> {

    Role findByRoleName(String role);
}