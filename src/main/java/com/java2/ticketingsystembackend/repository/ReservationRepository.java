package com.java2.ticketingsystembackend.repository;

import com.java2.ticketingsystembackend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByUser_Id(Integer userId);
}
