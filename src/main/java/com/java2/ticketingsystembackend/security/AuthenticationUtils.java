package com.java2.ticketingsystembackend.security;

import com.java2.ticketingsystembackend.entity.User;
import com.java2.ticketingsystembackend.exception.UnauthorizedException;
import com.java2.ticketingsystembackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtils {

    @Autowired
    private UserRepository userRepository;

    /// Authenticate and return the current user
    /// throw UnauthorizedException if the user is anon
    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getAuthorities().isEmpty() || auth.getPrincipal().equals("anonymousUser")) {
            throw new UnauthorizedException("Unauthorized: You need to log in to access this resource.");
        }

        /// Get user from database
        String username = auth.getPrincipal().toString();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("Unauthorized: User does not exist."));
    }
}

