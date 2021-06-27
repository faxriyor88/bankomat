package com.example.bankomat.repository;

import com.example.bankomat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
