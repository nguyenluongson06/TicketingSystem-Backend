package com.java2.ticketingsystembackend.controller;

import com.java2.ticketingsystembackend.entity.*;
import com.java2.ticketingsystembackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    ///TODO
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        userService.saveUser(user);
        return "User registered successfully";
    }

    ///TODO
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return "";
    }
}

