package com.java2.ticketingsystembackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, name = "url")
    private String url;

    @Enumerated(EnumType.STRING)
    private MediaType type = MediaType.IMAGE;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    public enum MediaType {
        IMAGE, VIDEO
    }
}

