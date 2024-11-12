package com.java2.ticketingsystembackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "events")
@Getter @Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(name = "time_start", nullable = false)
    private LocalDateTime timeStart;

    @Column(name = "time_end", nullable = false)
    private LocalDateTime timeEnd;

    @Column(name = "place")
    private String place;

    @Column(name = "description")
    private String description;

    @Column(name = "max_quantity")
    private Integer maxQuantity;


    @Column(name = "is_public")
    private int isPublic = 1;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;

    @Column(name = "organizer_uuid", nullable = true)
    private UUID organizerUuid;
}
