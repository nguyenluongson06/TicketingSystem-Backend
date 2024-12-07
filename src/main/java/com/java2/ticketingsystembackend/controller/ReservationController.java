package com.java2.ticketingsystembackend.controller;

import com.java2.ticketingsystembackend.dto.CreateReservationDTO;
import com.java2.ticketingsystembackend.dto.ReservationDTO;
import com.java2.ticketingsystembackend.dto.UpdateReservationDTO;
import com.java2.ticketingsystembackend.exception.EntityNotFoundException;
import com.java2.ticketingsystembackend.exception.UnauthorizedException;
import com.java2.ticketingsystembackend.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

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
}

