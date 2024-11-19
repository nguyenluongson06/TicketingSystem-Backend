package com.java2.ticketingsystembackend.repository;

import com.java2.ticketingsystembackend.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByEvent_Id(Integer eventId);
}