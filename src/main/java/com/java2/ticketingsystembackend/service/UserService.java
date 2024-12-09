package com.java2.ticketingsystembackend.service;

import com.java2.ticketingsystembackend.dto.SignupRequestDTO;
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
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public boolean registerUser(SignupRequestDTO signupRequestDTO) {
        if (userRepository.existsByUsername(signupRequestDTO.getUsername()) ||
                userRepository.existsByEmail(signupRequestDTO.getEmail())) {
            return false;
        }

        User user = new User();
        Role defaultRole = new Role(){};
        defaultRole.setId(1); ///set default role as USER
        user.setUuid(UUID.randomUUID().toString());
        user.setUsername(signupRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequestDTO.getPassword()));
        user.setEmail(signupRequestDTO.getEmail());
        user.setFullname(signupRequestDTO.getFullname());
        user.setTel(signupRequestDTO.getTel());
        user.setAddress(signupRequestDTO.getAddress());
        user.setRole(defaultRole);
        userRepository.save(user);
        System.out.println("New user uuid:" + user.getUuid());
        emailService.sendRegistrationEmail(user);
        return true;
    }
}


