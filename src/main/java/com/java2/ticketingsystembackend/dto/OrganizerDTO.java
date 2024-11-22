package com.java2.ticketingsystembackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrganizerDTO {
    private String uuid;
    private String fullName;

    public OrganizerDTO(String uuid, String fullName) {
        this.uuid = uuid;
        this.fullName = fullName;
    }
}
