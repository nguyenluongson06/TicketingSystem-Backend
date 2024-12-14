package com.java2.ticketingsystembackend.mapper;

import com.java2.ticketingsystembackend.dto.MediaDTO;
import com.java2.ticketingsystembackend.entity.Media;

public class MediaMapper {
    public static MediaDTO toMediaDTO(Media media) {
        MediaDTO mediaDTO = new MediaDTO();
        mediaDTO.setEventUuid(media.getEvent().getUuid());
        mediaDTO.setId(media.getId());
        mediaDTO.setUrl(media.getUrl());
        mediaDTO.setType(media.getType().name());
        return mediaDTO;
    }
}
