package com.java2.ticketingsystembackend.controller;

import com.java2.ticketingsystembackend.dto.CreateReservationDTO;
import com.java2.ticketingsystembackend.dto.ReservationDTO;
import com.java2.ticketingsystembackend.dto.UpdateReservationDTO;
import com.java2.ticketingsystembackend.entity.Reservation;
import com.java2.ticketingsystembackend.entity.User;
import com.java2.ticketingsystembackend.exception.EntityNotFoundException;
import com.java2.ticketingsystembackend.exception.UnauthorizedException;
import com.java2.ticketingsystembackend.repository.ReservationRepository;
import com.java2.ticketingsystembackend.security.AuthenticationUtils;
import com.java2.ticketingsystembackend.service.ReservationService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final AuthenticationUtils authenticationUtils;
    private final ReservationRepository reservationRepository;

    @PostMapping("/create")
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody CreateReservationDTO reservationDTO) {
        try {
            ReservationDTO createdReservation = reservationService.createReservation(reservationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<ReservationDTO>> getReservations() {
        try {
            List<ReservationDTO> reservations = reservationService.getReservations();
            return ResponseEntity.ok(reservations);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ReservationDTO> updateReservation(@RequestBody UpdateReservationDTO reservationDTO) {
        try {
            ReservationDTO updatedReservation = reservationService.updateReservation(reservationDTO);
            return ResponseEntity.ok(updatedReservation);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteReservation(@RequestParam String uuid) {
        try {
            reservationService.deleteReservation(uuid);
            return ResponseEntity.status(HttpStatus.OK).body("Reservation successfully deleted");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    ///reservation checkin, should be permit all to redirect to login page
    @GetMapping("/checkin/{reservationUuid}")
    @PermitAll
    public ResponseEntity<?> checkInReservation(@PathVariable String reservationUuid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Check if user is authenticated
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Redirect to login");
        }

        User user = authenticationUtils.getAuthenticatedUser();

        // Find the reservation by UUID
        Reservation reservation = reservationRepository.findByUuid(reservationUuid)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));

        // Check if the reservation belongs to the authenticated user
        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Unauthorized: This reservation does not belong to you.");
        }

        // Check if the reservation has already been used
        if (reservation.getIsUsed()) {
            return ResponseEntity.ok("This ticket was used.");
        }

        // Mark the reservation as used
        reservation.setIsUsed(true);
        reservationRepository.save(reservation);

        return ResponseEntity.ok("Check-in successful.");
    }

}

