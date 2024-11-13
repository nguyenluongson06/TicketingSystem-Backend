package com.java2.ticketingsystembackend.controller;

import com.java2.ticketingsystembackend.entity.Ticket;
import com.java2.ticketingsystembackend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/event/{eventId}")
    public List<Ticket> getTicketsForEvent(@PathVariable int eventId) {
        return ticketService.getAllTicketsForEvent(eventId);
    }

    @GetMapping("/{id}")
    public Ticket getTicketById(@PathVariable int id) {
        return ticketService.getTicketById(id).orElse(null);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public Ticket createTicket(@RequestBody Ticket ticket) {
        return ticketService.createTicket(ticket);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public Ticket updateTicket(@PathVariable int id, @RequestBody Ticket updatedTicket) {
        return ticketService.updateTicket(id, updatedTicket);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public String deleteTicket(@PathVariable int id) {
        ticketService.deleteTicket(id);
        return "Ticket deleted successfully";
    }
}
