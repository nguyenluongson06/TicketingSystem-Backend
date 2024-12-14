package com.java2.ticketingsystembackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class CreateEventDTO {
    private String name;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    private String place;
    private String description;
    private Integer maxQuantity;
    private Boolean isPublic;
    private Integer categoryId;
    private String thumbnail;

    public CreateEventDTO(String name, LocalDateTime timeStart, LocalDateTime timeEnd, String place, String description, Integer maxQuantity, Boolean isPublic, Integer categoryId) {
        this.name = name;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.place = place;
        this.description = description;
        this.maxQuantity = maxQuantity;
        this.isPublic = isPublic;
        this.categoryId = categoryId;
        this.thumbnail = "https://via.placeholder.com/343x197";
    }

    public CreateEventDTO(String name, LocalDateTime timeStart, LocalDateTime timeEnd, String place, String description, Integer maxQuantity, Boolean isPublic, Integer categoryId, String thumbnail) {
        this.name = name;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.place = place;
        this.description = description;
        this.maxQuantity = maxQuantity;
        this.isPublic = isPublic;
        this.categoryId = categoryId;
        this.thumbnail = thumbnail;
    }
}

