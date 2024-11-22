package com.java2.ticketingsystembackend.service;

import com.java2.ticketingsystembackend.dto.SignupRequest;
import com.java2.ticketingsystembackend.entity.Role;
import com.java2.ticketingsystembackend.entity.User;
import com.java2.ticketingsystembackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername()) ||
                userRepository.existsByEmail(signupRequest.getEmail())) {
            return false;
        }

        User user = new User();
        Role defaultRole = new Role(){};
        defaultRole.setId(1); ///set default role as USER
        user.setUuid(UUID.randomUUID().toString());
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setEmail(signupRequest.getEmail());
        user.setFullname(signupRequest.getFullname());
        user.setTel(signupRequest.getTel());
        user.setAddress(signupRequest.getAddress());
        user.setRole(defaultRole);
        userRepository.save(user);
        System.out.println("New user uuid:" + user.getUuid());
        return true;
    }
}


