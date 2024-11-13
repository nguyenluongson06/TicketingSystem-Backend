package com.java2.ticketingsystembackend.repository;

import com.java2.ticketingsystembackend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByUser(User user);
}