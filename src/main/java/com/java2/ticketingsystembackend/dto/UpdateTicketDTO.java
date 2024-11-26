package com.java2.ticketingsystembackend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTicketDTO {
    @NotBlank(message = "Ticket name is required")
    private String ticketName;

    @NotBlank(message = "Ticket type is required")
    private String ticketType;

    @NotBlank(message = "Ticket position is required")
    private String ticketPosition;

    @Positive(message = "Max quantity must be greater than zero")
    private Integer maxQuantity;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private double price;
}

