package com.java2.ticketingsystembackend.service;

import com.java2.ticketingsystembackend.dto.CreateTicketDTO;
import com.java2.ticketingsystembackend.dto.TicketDTO;
import com.java2.ticketingsystembackend.dto.UpdateTicketDTO;
import com.java2.ticketingsystembackend.entity.Event;
import com.java2.ticketingsystembackend.entity.Ticket;
import com.java2.ticketingsystembackend.entity.TicketInfo;
import com.java2.ticketingsystembackend.entity.User;
import com.java2.ticketingsystembackend.exception.EntityNotFoundException;
import com.java2.ticketingsystembackend.exception.UnauthorizedException;
import com.java2.ticketingsystembackend.repository.EventRepository;
import com.java2.ticketingsystembackend.repository.TicketInfoRepository;
import com.java2.ticketingsystembackend.repository.TicketRepository;
import com.java2.ticketingsystembackend.security.AuthenticationUtils;
import jakarta.validation.Valid;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final TicketInfoRepository ticketInfoRepository;
    private final AuthenticationUtils authenticationUtils;

    public TicketService(TicketRepository ticketRepository, EventRepository eventRepository, TicketInfoRepository ticketInfoRepository, AuthenticationUtils authenticationUtils) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.ticketInfoRepository = ticketInfoRepository;
        this.authenticationUtils = authenticationUtils;
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

    public List<TicketDTO> getTicketsByEventUuid(String eventUuid) {
        Optional<Event> eventOpt = eventRepository.findByUuid(eventUuid);
        if (eventOpt.isEmpty()) {
            throw new EntityNotFoundException("Event not found");
        }

        Event event = eventOpt.get();
        if (event.isPublic()){
            return ticketRepository.findByEventId(event.getId()).stream().map(this::convertToDTO).toList();
        } else {
            User authUser = authenticationUtils.getAuthenticatedUser();
            if (authUser.getRole().getName().equals("ADMIN") || (authUser.getRole().getName().equals("ORGANIZER") && event.getOrganizer().getId().equals(authUser.getId()))) {
                return ticketRepository.findByEventId(event.getId()).stream().map(this::convertToDTO).toList();
            } else {
                throw new UnauthorizedException("Unauthorized: You don't have permission to access this resource");
            }
        }
    }

    public TicketDTO createTicket(CreateTicketDTO dto) {
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        User user = authenticationUtils.getAuthenticatedUser();
        if (!user.getRole().getName().equals("ADMIN") &&
                !event.getOrganizer().getId().equals(user.getId())) {
            System.out.println("unauthorized");
            throw new UnauthorizedException("You don't have permission to create tickets for this event");
        }

        System.out.println("creating ticket info");
        TicketInfo ticketInfo = new TicketInfo();
        ticketInfo.setTicketCode(dto.getTicketCode());
        ticketInfo.setTicketName(dto.getTicketName());
        ticketInfo.setTicketType(dto.getTicketType());
        ticketInfo.setTicketPosition(dto.getTicketPosition());
        ticketInfo.setMaxQuantity(dto.getMaxQuantity());
        ticketInfo.setPrice(dto.getPrice());
        ticketInfo = ticketInfoRepository.save(ticketInfo);

        System.out.println("creating new ticket");
        Ticket ticket = new Ticket();
        ticket.setInfo(ticketInfo);
        ticket.setEvent(event);
        return convertToDTO(ticketRepository.save(ticket));
    }

    public TicketDTO updateTicket(int ticketId, @Valid UpdateTicketDTO dto) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

        User user = authenticationUtils.getAuthenticatedUser();
        if (!user.getRole().getName().equals("ADMIN") &&
                !ticket.getEvent().getOrganizer().getId().equals(user.getId())) {
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

    public void deleteTicket(int ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

        User user = authenticationUtils.getAuthenticatedUser();

        if (!user.getRole().getName().equals("ADMIN") &&
                !ticket.getEvent().getOrganizer().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to delete this ticket");
        }

        ticketRepository.delete(ticket);
    }

    public TicketDTO getTicketInfoWithTicketId(int ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

        if (ticket.getEvent().isPublic()){
            return convertToDTO(ticket);
        }

        User user = authenticationUtils.getAuthenticatedUser();
        if (!user.getRole().getName().equals("ADMIN") && !ticket.getEvent().getOrganizer().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to view this ticket");
        }

        return convertToDTO(ticket);
    }

    private TicketDTO convertToDTO(Ticket ticket) {
        return new TicketDTO(
                ticket.getId(),
                ticket.getInfo().getTicketCode(),
                ticket.getInfo().getTicketName(),
                ticket.getInfo().getTicketType(),
                ticket.getInfo().getTicketPosition(),
                ticket.getInfo().getMaxQuantity(),
                ticket.getInfo().getPrice()
                );
    }
}


