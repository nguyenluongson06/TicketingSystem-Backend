package com.java2.ticketingsystembackend.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tickets")
@Getter @Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "info_id", nullable = false)
    private TicketInfo info;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
}