package com.java2.ticketingsystembackend.controller;

import com.java2.ticketingsystembackend.dto.LoginRequest;
import com.java2.ticketingsystembackend.dto.SignupRequestDTO;
import com.java2.ticketingsystembackend.dto.UserInfoDTO;
import com.java2.ticketingsystembackend.entity.User;
import com.java2.ticketingsystembackend.mapper.UserMapper;
import com.java2.ticketingsystembackend.security.JwtTokenProvider;
import com.java2.ticketingsystembackend.service.AuthService;
import com.java2.ticketingsystembackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final AuthService authService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.authService = authService;
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

    @GetMapping("/info")
    public ResponseEntity<UserInfoDTO> info() {
        System.out.println("User getting info");
        try {
            System.out.println("getting current user");
            User currentUser = authService.getCurrentUser();
            System.out.println("retrieved current user");

            System.out.println("mapping current user");
            UserInfoDTO result = UserMapper.toUserInfoDTO(currentUser);

            System.out.println("sending result");
            return ResponseEntity.ok(result);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}


