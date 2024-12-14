package com.java2.ticketingsystembackend.controller;

import com.java2.ticketingsystembackend.dto.LoginRequest;
import com.java2.ticketingsystembackend.dto.SignupRequestDTO;
import com.java2.ticketingsystembackend.security.JwtTokenProvider;
import com.java2.ticketingsystembackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        String token = jwtTokenProvider.generateToken(authentication.getName());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDTO signupRequestDTO) {
        System.out.println("New user signing up");
        if (!userService.registerUser(signupRequestDTO)) {
            return ResponseEntity.badRequest().body("Username or email already exists.");
        }
        return ResponseEntity.ok("User registered successfully!");
    }
}


