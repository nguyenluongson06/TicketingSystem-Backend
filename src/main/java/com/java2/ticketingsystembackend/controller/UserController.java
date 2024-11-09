package com.java2.ticketingsystembackend.controller;

import com.java2.ticketingsystembackend.model.Role;
import com.java2.ticketingsystembackend.model.User;
import com.java2.ticketingsystembackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        if (userService.userExists(user.getUsername(), user.getEmail())) {
            return ResponseEntity.badRequest().body("Username or Email already exists");
        }

        user.setRole(new Role());
        user.getRole().setId(1);

        userService.saveUser(user);

        return ResponseEntity.ok("User registered successfully");
    }
}