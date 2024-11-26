package com.java2.ticketingsystembackend.dto;

import com.java2.ticketingsystembackend.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO {
    private Integer id;
    private String ticketCode;
    private String ticketName;
    private String ticketType;
    private String ticketPosition;
    private Integer maxQuantity;
    private double price;

    public static TicketDTO fromEntity(Ticket ticket) {
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
