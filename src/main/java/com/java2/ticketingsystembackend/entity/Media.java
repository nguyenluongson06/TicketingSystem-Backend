package com.java2.ticketingsystembackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    private MediaType type = MediaType.image;

    public enum MediaType {
        image,
        video
    }
}

