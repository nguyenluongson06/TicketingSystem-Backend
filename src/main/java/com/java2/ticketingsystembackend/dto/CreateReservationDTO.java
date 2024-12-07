package com.java2.ticketingsystembackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateReservationDTO {
    @NotNull
    private Integer ticketId;
    @NotNull
    private Integer quantity;
}

