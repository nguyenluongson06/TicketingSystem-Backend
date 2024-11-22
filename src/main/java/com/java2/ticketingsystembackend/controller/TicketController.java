package com.java2.ticketingsystembackend.controller;

import com.java2.ticketingsystembackend.dto.TicketCreateDTO;
import com.java2.ticketingsystembackend.dto.TicketDTO;
import com.java2.ticketingsystembackend.dto.TicketUpdateDTO;
import com.java2.ticketingsystembackend.entity.User;
import com.java2.ticketingsystembackend.service.AuthService;
import com.java2.ticketingsystembackend.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final AuthService authService;

    public TicketController(TicketService ticketService, AuthService authService) {
        this.ticketService = ticketService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getTickets() {
        User currentUser = authService.getCurrentUser();
        List<TicketDTO> tickets = ticketService.getTicketsByRole(currentUser);
        return ResponseEntity.ok(tickets);
    }

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@RequestBody @Valid TicketCreateDTO dto) {
        User currentUser = authService.getCurrentUser();
        TicketDTO ticket = ticketService.createTicket(dto, currentUser);
        return new ResponseEntity<>(ticket, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable int id, @RequestBody @Valid TicketUpdateDTO dto) {
        User currentUser = authService.getCurrentUser();
        TicketDTO ticket = ticketService.updateTicket(id, dto, currentUser);
        return ResponseEntity.ok(ticket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable int id) {
        User currentUser = authService.getCurrentUser();
        ticketService.deleteTicket(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketInfo(@PathVariable int id) {
        User currentUser = authService.getCurrentUser();
        TicketDTO ticket = ticketService.getTicketInfo(id, currentUser);
        return ResponseEntity.ok(ticket);
    }
}
