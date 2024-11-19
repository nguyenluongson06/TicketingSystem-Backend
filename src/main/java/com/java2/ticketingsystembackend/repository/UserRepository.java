package com.java2.ticketingsystembackend.repository;

import com.java2.ticketingsystembackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUuid(String uuid);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
