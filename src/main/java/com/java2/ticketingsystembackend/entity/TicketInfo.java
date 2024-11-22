package com.java2.ticketingsystembackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter @Setter
@Table(name = "ticket_info")
public class TicketInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ticketCode;

    private String ticketName;

    private String ticketType;

    private String ticketPosition;

    private Integer maxQuantity;

    @Column(nullable = false)
    private Double price;
}

