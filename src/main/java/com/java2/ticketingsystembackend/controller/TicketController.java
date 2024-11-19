package com.java2.ticketingsystembackend.controller;

import com.java2.ticketingsystembackend.entity.Ticket;
import com.java2.ticketingsystembackend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Ticket>> getAllTicketsForEvent(@PathVariable Integer eventId) {
        List<Ticket> tickets = ticketService.getAllTicketsForEvent(eventId);
        return ResponseEntity.ok(tickets);
    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestParam Integer eventId, @RequestParam Integer ticketInfoId) {
        Ticket ticket = ticketService.createTicket(eventId, ticketInfoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }
}

