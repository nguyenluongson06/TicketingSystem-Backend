package com.java2.ticketingsystembackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ReservationDTO {
    private String uuid;
    private Integer ticketId;
    private Integer quantity;
    private LocalDateTime reservationDate;
    private boolean isUsed;
}

