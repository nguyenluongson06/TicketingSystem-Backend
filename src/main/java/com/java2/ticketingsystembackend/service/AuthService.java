    package com.java2.ticketingsystembackend.service;

    import com.java2.ticketingsystembackend.entity.User;
    import com.java2.ticketingsystembackend.exception.UnauthorizedException;
    import com.java2.ticketingsystembackend.repository.UserRepository;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.stereotype.Service;

    @Service
    public class AuthService {

        private final Logger logger = LoggerFactory.getLogger(AuthService.class);
        private final UserRepository userRepository;

        public AuthService(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        public User getCurrentUser() {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }
    }

