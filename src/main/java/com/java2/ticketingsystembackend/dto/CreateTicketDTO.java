package com.java2.ticketingsystembackend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTicketDTO {
    @NotBlank
    private String ticketCode;
    @NotBlank
    private String ticketName;
    @NotBlank
    private String ticketType;
    @NotBlank
    private String ticketPosition;
    @Min(1)
    private Integer maxQuantity;
    @DecimalMin("0.0")
    private double price;
    @NotNull
    private Integer eventId;
}

