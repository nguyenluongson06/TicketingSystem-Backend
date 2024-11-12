package com.java2.ticketingsystembackend.service;

import com.java2.ticketingsystembackend.entity.*;
import com.java2.ticketingsystembackend.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    public List<Reservation> getReservationsByUser(User user) {
        return reservationRepository.findByUser(user);
    }

    public List<Reservation> getReservationByUserId(int id){
        try {
            if (userRepository.findById(id).isPresent()) {
                User user = userRepository.findById(id).get();
                return reservationRepository.findByUser(user);
            }
        } catch (Exception e) {
            System.out.println("Cannot get reservation" + e.getMessage());
        }
        return null;
    }

    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void deleteReservation(int id) {
        reservationRepository.deleteById(id);
    }
}
