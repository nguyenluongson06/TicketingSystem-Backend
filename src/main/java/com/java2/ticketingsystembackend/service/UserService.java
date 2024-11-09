package com.java2.ticketingsystembackend.service;

import com.java2.ticketingsystembackend.model.User;
import com.java2.ticketingsystembackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean userExists(String username, String email) {
        return userRepository.findByUsername(username) != null || userRepository.findByEmail(email) != null;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
