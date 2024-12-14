package com.java2.ticketingsystembackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class EventDTO {
    private String uuid;
    private String name;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
    private String place;
    private String description;
    private int maxQuantity;
    private boolean isPublic;
    private OrganizerDTO organizer;
    private String categoryName;
    private String thumbnailUrl;

    // Constructor
    public EventDTO(String uuid, String name, LocalDateTime timeStart, LocalDateTime timeEnd,
                    String place, String description, int maxQuantity, boolean isPublic,
                    OrganizerDTO organizer, String categoryName, String thumbnailUrl) {
        this.uuid = uuid;
        this.name = name;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.place = place;
        this.description = description;
        this.maxQuantity = maxQuantity;
        this.isPublic = isPublic;
        this.organizer = organizer;
        this.categoryName = categoryName;
        this.thumbnailUrl = thumbnailUrl;
    }
}