package com.java2.ticketingsystembackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class UpdateEventDTO {
    private String name;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    private String place;
    private String description;
    private Integer maxQuantity;
    private Boolean isPublic;
    private Integer categoryId;

    public UpdateEventDTO(String name, LocalDateTime timeStart, LocalDateTime timeEnd, String place, String description, Integer maxQuantity, Boolean isPublic, Integer categoryId) {
        this.name = name;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.place = place;
        this.description = description;
        this.maxQuantity = maxQuantity;
        this.isPublic = isPublic;
        this.categoryId = categoryId;
    }
    // Getters and setters
}

