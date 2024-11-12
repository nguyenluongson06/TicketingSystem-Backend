package com.java2.ticketingsystembackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, name = "tier_name")
    private String tierName;

    @Column(nullable = false, name = "tier_price")
    private Double tierPrice;

    @Column(nullable = false, name = "max_quantity")
    private Integer maxQuantity;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
}

