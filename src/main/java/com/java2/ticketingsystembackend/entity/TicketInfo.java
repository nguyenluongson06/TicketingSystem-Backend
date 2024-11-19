package com.java2.ticketingsystembackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@Entity
@Table(name = "ticket_info")
public class TicketInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(unique = true, nullable = false, name = "ticket_code")
    private String ticketCode;

    @Column(nullable = false, name = "ticket_name")
    private String ticketName;

    @Column(nullable = false, name = "ticket_type")
    private String ticketType;

    @Column(nullable = false, name = "position")
    private String position;

    @Column(nullable = false, name = "max_quantity")
    private Integer maxQuantity;

    @Column(nullable = false, name = "price")
    private BigDecimal price;
}

