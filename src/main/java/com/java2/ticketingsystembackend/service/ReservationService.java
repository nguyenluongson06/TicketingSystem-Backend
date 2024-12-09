package com.java2.ticketingsystembackend.service;

import com.java2.ticketingsystembackend.dto.CreateReservationDTO;
import com.java2.ticketingsystembackend.dto.ReservationDTO;
import com.java2.ticketingsystembackend.dto.UpdateReservationDTO;
import com.java2.ticketingsystembackend.entity.Event;
import com.java2.ticketingsystembackend.entity.Reservation;
import com.java2.ticketingsystembackend.entity.Ticket;
import com.java2.ticketingsystembackend.entity.User;
import com.java2.ticketingsystembackend.exception.EntityNotFoundException;
import com.java2.ticketingsystembackend.exception.UnauthorizedException;
import com.java2.ticketingsystembackend.repository.EventRepository;
import com.java2.ticketingsystembackend.repository.ReservationRepository;
import com.java2.ticketingsystembackend.repository.TicketRepository;
import com.java2.ticketingsystembackend.security.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final EventRepository eventRepository;
    private final AuthenticationUtils authenticationUtils;
    private final TicketRepository ticketRepository;
    private final EmailService emailService;

    // Create reservation
    public ReservationDTO createReservation(CreateReservationDTO reservationDTO) {
        User user = authenticationUtils.getAuthenticatedUser();

        if (!user.getRole().getName().equals("USER")) {
            throw new UnauthorizedException("Only registered users can make reservations.");
        }

        Optional<Ticket> ticketOpt = ticketRepository.findById(reservationDTO.getTicketId());
        if (ticketOpt.isEmpty()){
            throw new EntityNotFoundException("Ticket not found");
        }

        Ticket ticket = ticketOpt.get();
        Event event = ticket.getEvent();

        if (!event.isPublic()) {
            throw new UnauthorizedException("Reservations cannot be made for private events.");
        }

        Reservation reservation = new Reservation();
        reservation.setUuid(UUID.randomUUID().toString());
        reservation.setUser(user);
        reservation.setTicket(ticket);
        reservation.setQuantity(reservationDTO.getQuantity());
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setIsUsed(false);
        reservationRepository.save(reservation);
        String checkinURL = "http://localhost:8000/api/reservations/checkin/" + reservation.getUuid();
        emailService.sendReservationEmail(user.getEmail(), user.getFullname(), event.getName(), checkinURL);

        return toReservationDTO(reservation);
    }

    // Get reservation list
    public List<ReservationDTO> getReservations() {
        User user = authenticationUtils.getAuthenticatedUser();

        if (user.getRole().getName().equals("ADMIN")) {
            return reservationRepository.findAll().stream()
                    .map(this::toReservationDTO)
                    .collect(Collectors.toList());
        } else if (user.getRole().getName().equals("USER")) {
            return reservationRepository.findByUserId(user.getId()).stream()
                    .map(this::toReservationDTO)
                    .collect(Collectors.toList());
        }

        throw new UnauthorizedException("Unauthorized to access reservations.");
    }

    // Update reservation
    public ReservationDTO updateReservation(UpdateReservationDTO reservationDTO) {
        User user = authenticationUtils.getAuthenticatedUser();

        if (!user.getRole().getName().equals("USER")) {
            throw new UnauthorizedException("Only registered users can update reservations.");
        }

        Optional<Reservation> reservationOpt = reservationRepository.findByUuid(reservationDTO.getUuid());
        if (reservationOpt.isEmpty()){
            throw new EntityNotFoundException("Reservation not found");
        }
        Reservation reservation = reservationOpt.get();

        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Unauthorized to update this reservation.");
        }

        reservation.setQuantity(reservationDTO.getQuantity());
        reservation = reservationRepository.save(reservation);

        return toReservationDTO(reservation);
    }

    // Delete reservation
    public void deleteReservation(String reservationUuid) {
        User user = authenticationUtils.getAuthenticatedUser();

        if (!user.getRole().getName().equals("USER")) {
            throw new UnauthorizedException("Only registered users can delete reservations.");
        }

        Optional<Reservation> reservationOpt = reservationRepository.findByUuid(reservationUuid);
        if (reservationOpt.isEmpty()){
            throw new EntityNotFoundException("Reservation not found");
        }
        Reservation reservation = reservationOpt.get();

        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Unauthorized to delete this reservation.");
        }

        reservationRepository.delete(reservation);
    }



    // Utility method: Convert Reservation to DTO
    private ReservationDTO toReservationDTO(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setUuid(reservation.getUuid());
        dto.setTicketId(reservation.getTicket().getId());
        dto.setReservationDate(reservation.getReservationDate());
        dto.setQuantity(reservation.getQuantity());
        dto.setUsed(reservation.getIsUsed());
        return dto;
    }
}

