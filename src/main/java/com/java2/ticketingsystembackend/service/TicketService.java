package com.java2.ticketingsystembackend.service;

import com.java2.ticketingsystembackend.entity.Ticket;
import com.java2.ticketingsystembackend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> getAllTicketsForEvent(int eventId) {
        return ticketRepository.findByEventId(eventId);
    }

    public Optional<Ticket> getTicketById(int id) {
        return ticketRepository.findById(id);
    }

    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Ticket updateTicket(int id, Ticket updatedTicket) {
        return ticketRepository.findById(id).map(ticket -> {
            ticket.setTierName(updatedTicket.getTierName());
            ticket.setTierPrice(updatedTicket.getTierPrice());
            ticket.setMaxQuantity(updatedTicket.getMaxQuantity());
            return ticketRepository.save(ticket);
        }).orElse(null);
    }

    public void deleteTicket(int id) {
        ticketRepository.deleteById(id);
    }
}
