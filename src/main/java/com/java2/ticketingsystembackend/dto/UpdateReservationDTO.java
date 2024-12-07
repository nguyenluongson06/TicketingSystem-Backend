package com.java2.ticketingsystembackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateReservationDTO {
    @NotNull
    private String uuid;
    @NotNull
    private Integer quantity;
}
