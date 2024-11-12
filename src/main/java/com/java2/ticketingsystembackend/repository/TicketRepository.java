package com.java2.ticketingsystembackend.repository;

import com.java2.ticketingsystembackend.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByEventId(int eventId);
}
