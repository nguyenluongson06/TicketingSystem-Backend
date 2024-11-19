﻿package com.java2.ticketingsystembackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "media")
@Entity
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
    private MediaType type;

    public enum MediaType {
        IMAGE, VIDEO
    }
}


