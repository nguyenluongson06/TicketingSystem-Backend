package com.java2.ticketingsystembackend.repository;

import com.java2.ticketingsystembackend.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByEventId(Integer eventId); // Get tickets by event
    @Query("SELECT t FROM Ticket t WHERE t.event.isPublic = true OR t.event.organizer.id = :organizerId")
    List<Ticket> findByVisibilityForOrganizer(@Param("organizerId") Integer organizerId); // Visible tickets for organizer

    List<Ticket> findByEventOrganizerId(Integer id);

    @Query("SELECT t FROM Ticket t WHERE t.event.isPublic = true") // Example condition
    List<Ticket> findPublicTickets();

}

