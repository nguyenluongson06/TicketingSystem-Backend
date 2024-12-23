package com.java2.ticketingsystembackend.config;

import com.java2.ticketingsystembackend.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Apply CORS to all endpoints
                        .allowedOrigins("http://localhost:3000") // Allow frontend origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Include OPTIONS
                        .allowedHeaders("Content-Type", "Authorization") // Include headers sent by frontend
                        .exposedHeaders("Authorization") // If you return token/headers in the response
                        .allowCredentials(true); // Allow cookies if needed
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/events/list").permitAll()// Allow open access to register/login
                                .requestMatchers("/api/events/info/**").permitAll()//permit all to get info using uuid
                                .requestMatchers("/api/events/info").hasAnyRole("ADMIN", "ORGANIZER")
                                .requestMatchers("/api/tickets/event/*").permitAll()//only auth users can get info using id
                                .requestMatchers("/api/reservations").authenticated()
                                .requestMatchers("/api/reservations/**").authenticated()
                                .requestMatchers("/api/reservations/").authenticated()
                                .requestMatchers("api/reservations/delete/**").authenticated()
                                .requestMatchers("api/reservations/create").authenticated()
                                .requestMatchers("/api/reservations/checkin/**").permitAll()
                                .requestMatchers("/api/email/test").authenticated()
                                .requestMatchers("/api/email/testqr").authenticated()
                                .requestMatchers("/api/categories").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/media/event/**").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")     // Only admins can access /admin endpoints
                                .requestMatchers("/organizer/**").hasRole("ORGANIZER")  // Only organizers can access /organizer endpoints
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .anyRequest().authenticated()  // Require authentication for all other requests
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(logout -> logout
                        .logoutUrl("/logout")  // Customize logout URL if needed
                        .permitAll())
                .csrf(AbstractHttpConfigurer::disable).cors();  // Disable CSRF if you're using stateless authentication (e.g., JWT)

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }
}
