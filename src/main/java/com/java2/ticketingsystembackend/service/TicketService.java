package com.java2.ticketingsystembackend.service;

import com.java2.ticketingsystembackend.dto.TicketCreateDTO;
import com.java2.ticketingsystembackend.dto.TicketDTO;
import com.java2.ticketingsystembackend.dto.TicketUpdateDTO;
import com.java2.ticketingsystembackend.entity.Event;
import com.java2.ticketingsystembackend.entity.Ticket;
import com.java2.ticketingsystembackend.entity.TicketInfo;
import com.java2.ticketingsystembackend.entity.User;
import com.java2.ticketingsystembackend.repository.EventRepository;
import com.java2.ticketingsystembackend.repository.TicketInfoRepository;
import com.java2.ticketingsystembackend.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final TicketInfoRepository ticketInfoRepository;

    public TicketService(TicketRepository ticketRepository, EventRepository eventRepository, TicketInfoRepository ticketInfoRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.ticketInfoRepository = ticketInfoRepository;
    }

    public List<TicketDTO> getTicketsByRole(User user) {
        List<Ticket> tickets;

        if (user.getRole().getName().equals("ADMIN")) {
            tickets = ticketRepository.findAll();
        } else if (user.getRole().getName().equals("ORGANIZER")) {
            tickets = ticketRepository.findByEventOrganizerId(user.getId());
        } else {
            tickets = ticketRepository.findPublicTickets();
        }

        return tickets.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public TicketDTO createTicket(TicketCreateDTO dto, User currentUser) {
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        if (!event.isPublic() && !currentUser.getRole().getName().equals("ADMIN") &&
                !event.getOrganizer().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You don't have permission to create tickets for this event");
        }

        TicketInfo ticketInfo = new TicketInfo();
        ticketInfo.setTicketCode(UUID.randomUUID().toString());
        ticketInfo.setTicketName(dto.getTicketName());
        ticketInfo.setTicketType(dto.getTicketType());
        ticketInfo.setTicketPosition(dto.getTicketPosition());
        ticketInfo.setMaxQuantity(dto.getMaxQuantity());
        ticketInfo.setPrice(dto.getPrice());
        ticketInfo = ticketInfoRepository.save(ticketInfo);

        Ticket ticket = new Ticket();
        ticket.setInfo(ticketInfo);
        ticket.setEvent(event);
        return convertToDTO(ticketRepository.save(ticket));
    }

    public TicketDTO updateTicket(int ticketId, @Valid TicketUpdateDTO dto, User currentUser) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

        if (!currentUser.getRole().getName().equals("ADMIN") &&
                !ticket.getEvent().getOrganizer().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You don't have permission to update this ticket");
        }

        TicketInfo ticketInfo = ticket.getInfo();
        ticketInfo.setTicketName(dto.getTicketName());
        ticketInfo.setTicketType(dto.getTicketType());
        ticketInfo.setTicketPosition(dto.getTicketPosition());
        ticketInfo.setMaxQuantity(dto.getMaxQuantity());
        ticketInfo.setPrice(dto.getPrice());
        ticketInfoRepository.save(ticketInfo);

        return convertToDTO(ticket);
    }

    public void deleteTicket(int ticketId, User currentUser) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

        if (!currentUser.getRole().getName().equals("ADMIN") &&
                !ticket.getEvent().getOrganizer().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You don't have permission to delete this ticket");
        }

        ticketRepository.delete(ticket);
    }

    public TicketDTO getTicketInfo(int ticketId, User currentUser) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

        if (!ticket.getEvent().isPublic() &&
                !currentUser.getRole().getName().equals("ADMIN") &&
                !ticket.getEvent().getOrganizer().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You don't have permission to view this ticket");
        }

        return convertToDTO(ticket);
    }

    private TicketDTO convertToDTO(Ticket ticket) {
        TicketInfo info = ticket.getInfo();
        return new TicketDTO(
                ticket.getId(),
                info.getTicketCode(),
                info.getTicketName(),
                info.getTicketType(),
                info.getTicketPosition(),
                info.getMaxQuantity(),
                info.getPrice(),
                ticket.getEvent().getId()
        );
    }
}


