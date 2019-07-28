package com.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.entities.auth.User;

public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);
}

