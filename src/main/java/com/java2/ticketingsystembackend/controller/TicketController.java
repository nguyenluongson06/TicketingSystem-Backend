package com.java2.ticketingsystembackend.controller;

import com.java2.ticketingsystembackend.dto.CreateTicketDTO;
import com.java2.ticketingsystembackend.dto.TicketDTO;
import com.java2.ticketingsystembackend.dto.UpdateTicketDTO;
import com.java2.ticketingsystembackend.entity.User;
import com.java2.ticketingsystembackend.exception.EntityNotFoundException;
import com.java2.ticketingsystembackend.exception.UnauthorizedException;
import com.java2.ticketingsystembackend.service.AuthService;
import com.java2.ticketingsystembackend.service.TicketService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final AuthService authService;

    public TicketController(TicketService ticketService, AuthService authService) {
        this.ticketService = ticketService;
        this.authService = authService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<TicketDTO>> getTickets() {
        User currentUser = authService.getCurrentUser();
        List<TicketDTO> tickets = ticketService.getTicketsByRole(currentUser);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/event/{uuid}")
    @PermitAll
    public ResponseEntity<List<TicketDTO>> getTicketsOfEvent(@PathVariable String uuid) {
        try {
            List<TicketDTO> tickets = ticketService.getTicketsByEventUuid(uuid);
            return ResponseEntity.ok(tickets);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<TicketDTO> createTicket(@RequestBody @Valid CreateTicketDTO dto) {
        try {
            TicketDTO ticket = ticketService.createTicket(dto);
            return new ResponseEntity<>(ticket, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable int id, @RequestBody @Valid UpdateTicketDTO dto) {
        try {
            TicketDTO ticket = ticketService.updateTicket(id, dto);
            return ResponseEntity.ok(ticket);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable int id) {
        try {
            ticketService.deleteTicket(id);
            return ResponseEntity.status(HttpStatus.OK).body("Ticket deleted");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketInfo(@PathVariable int id) {
        try {
            TicketDTO ticket = ticketService.getTicketInfoWithTicketId(id);
            return ResponseEntity.ok(ticket);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }
}
