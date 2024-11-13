package com.java2.ticketingsystembackend.controller;

import com.java2.ticketingsystembackend.entity.*;
import com.java2.ticketingsystembackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @PostMapping("/reserve")
    public Reservation reserveEvent(@RequestBody Reservation reservation, Authentication auth) {
        User user = (User) auth.getPrincipal();
        reservation.setUser(user);
        return reservationService.saveReservation(reservation);
    }

    @PutMapping("/{id}")
    public Reservation reserveChange(@PathVariable Long id, @RequestBody Reservation updatedReservation) {
        // Implement check if the reservation belongs to the logged-in user
        return reservationService.saveReservation(updatedReservation);
    }

    @GetMapping("/user")
    public List<Reservation> getRegisteredEvent(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return reservationService.getReservationsByUser(user);
    }
}
