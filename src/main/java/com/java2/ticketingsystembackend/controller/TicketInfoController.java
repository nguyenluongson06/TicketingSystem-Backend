package com.java2.ticketingsystembackend.controller;

import com.java2.ticketingsystembackend.entity.TicketInfo;
import com.java2.ticketingsystembackend.service.TicketInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticket-info")
public class TicketInfoController {

    private final TicketInfoService ticketInfoService;

    @Autowired
    public TicketInfoController(TicketInfoService ticketInfoService) {
        this.ticketInfoService = ticketInfoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketInfo> getTicketInfoById(@PathVariable Integer id) {
        TicketInfo ticketInfo = ticketInfoService.getTicketInfoById(id);
        return ResponseEntity.ok(ticketInfo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketInfo> updateTicketInfo(@PathVariable Integer id, @RequestBody TicketInfo updatedInfo) {
        TicketInfo ticketInfo = ticketInfoService.updateTicketInfo(id, updatedInfo);
        return ResponseEntity.ok(ticketInfo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicketInfo(@PathVariable Integer id) {
        ticketInfoService.deleteTicketInfo(id);
        return ResponseEntity.noContent().build();
    }
}

