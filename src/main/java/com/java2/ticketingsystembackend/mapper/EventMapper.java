package com.java2.ticketingsystembackend.mapper;

import com.java2.ticketingsystembackend.dto.EventDTO;
import com.java2.ticketingsystembackend.dto.OrganizerDTO;
import com.java2.ticketingsystembackend.entity.Event;

public class EventMapper {

    public static EventDTO toEventDTO(Event event) {
        OrganizerDTO organizerDTO = new OrganizerDTO(
                event.getOrganizer().getUuid(),
                event.getOrganizer().getFullname()
        );

        return new EventDTO(
                event.getUuid(),
                event.getName(),
                event.getTimeStart(),
                event.getTimeEnd(),
                event.getPlace(),
                event.getDescription(),
                event.getMaxQuantity(),
                event.getIsPublic(),
                organizerDTO,
                event.getCategory().getName(),
                event.getThumbnail()
        );
    }
}

