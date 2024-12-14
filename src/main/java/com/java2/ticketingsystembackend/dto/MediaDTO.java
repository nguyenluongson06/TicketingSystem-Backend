package com.java2.ticketingsystembackend.dto;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MediaDTO {
    private String eventUuid;
    private Integer id;
    private String url;
    private String type; // e.g., image, video, etc.
}

