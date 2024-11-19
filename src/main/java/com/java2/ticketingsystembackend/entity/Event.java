package com.java2.ticketingsystembackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table(name = "events")
@Getter @Setter
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(unique = true, nullable = false, name = "uuid")
    private String uuid = UUID.randomUUID().toString();

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "time_start")
    private LocalDateTime timeStart;

    @Column(nullable = false, name = "time_end")
    private LocalDateTime timeEnd;

    @Column(nullable = false, name = "place")
    private String place;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;

    @Column(nullable = false, name = "description")
    private String description;

    @Column(nullable = false, name = "max_quantity")
    private Integer maxQuantity;

    @Column(nullable = false, name = "is_public")
    private Boolean isPublic = true;
}
