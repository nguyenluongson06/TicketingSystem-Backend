package com.java2.ticketingsystembackend.service;

import com.java2.ticketingsystembackend.entity.Event;
import com.java2.ticketingsystembackend.entity.Ticket;
import com.java2.ticketingsystembackend.entity.TicketInfo;
import com.java2.ticketingsystembackend.repository.EventRepository;
import com.java2.ticketingsystembackend.repository.TicketInfoRepository;
import com.java2.ticketingsystembackend.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketInfoRepository ticketInfoRepository;
    private final EventRepository eventRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository,
                         TicketInfoRepository ticketInfoRepository,
                         EventRepository eventRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketInfoRepository = ticketInfoRepository;
        this.eventRepository = eventRepository;
    }

    public List<Ticket> getAllTicketsForEvent(Integer eventId) {
        return ticketRepository.findByEvent_Id(eventId);
    }

    public Optional<Ticket> getTicketById(int id) {
        return ticketRepository.findById(id);
    }

    public Ticket createTicket(Integer eventId, Integer ticketInfoId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        TicketInfo ticketInfo = ticketInfoRepository.findById(ticketInfoId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket Info not found"));

        Ticket ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setInfo(ticketInfo);
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(int id) {
        ticketRepository.deleteById(id);
    }
}
