package com.java2.ticketingsystembackend.service;

import com.java2.ticketingsystembackend.dto.CreateMediaDTO;
import com.java2.ticketingsystembackend.dto.MediaDTO;
import com.java2.ticketingsystembackend.entity.Event;
import com.java2.ticketingsystembackend.entity.Media;
import com.java2.ticketingsystembackend.entity.User;
import com.java2.ticketingsystembackend.exception.EntityNotFoundException;
import com.java2.ticketingsystembackend.exception.UnauthorizedException;
import com.java2.ticketingsystembackend.mapper.MediaMapper;
import com.java2.ticketingsystembackend.repository.EventRepository;
import com.java2.ticketingsystembackend.repository.MediaRepository;
import com.java2.ticketingsystembackend.security.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AuthenticationUtils authenticationUtils;

    public List<MediaDTO> getMediaByEventUuid(String eventUuid) {
        Event event = eventRepository.findByUuid(eventUuid)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        return mediaRepository.findAllByEventId(event.getId()).stream()
                .map(MediaMapper::toMediaDTO)
                .toList();
    }

    public Media addMediaToEvent(Integer eventId, CreateMediaDTO mediaDTO) {
        User user = authenticationUtils.getAuthenticatedUser();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        if (!user.getRole().getName().equals("ADMIN") &&
                !(user.getRole().getName().equals("ORGANIZER") && event.getOrganizer().getId().equals(user.getId()))) {
            throw new UnauthorizedException("Unauthorized: You do not have permission to add media to this event.");
        }

        Media media = new Media();
        media.setEvent(event);
        media.setUrl(mediaDTO.getUrl());
        media.setType(Media.MediaType.valueOf(mediaDTO.getType()));
        return mediaRepository.save(media);
    }

    public Media updateMedia(Integer mediaId, CreateMediaDTO mediaDTO) {
        User user = authenticationUtils.getAuthenticatedUser();
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new EntityNotFoundException("Media not found"));

        if (!user.getRole().getName().equals("ADMIN") &&
                !(user.getRole().getName().equals("ORGANIZER") && media.getEvent().getOrganizer().getId().equals(user.getId()))) {
            throw new UnauthorizedException("Unauthorized: You do not have permission to update this media.");
        }

        media.setUrl(mediaDTO.getUrl());
        media.setType(Media.MediaType.valueOf(mediaDTO.getType()));
        return mediaRepository.save(media);
    }

    public void deleteMedia(Integer mediaId) {
        User user = authenticationUtils.getAuthenticatedUser();
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new EntityNotFoundException("Media not found"));

        if (!user.getRole().getName().equals("ADMIN") &&
                !(user.getRole().getName().equals("ORGANIZER") && media.getEvent().getOrganizer().getId().equals(user.getId()))) {
            throw new UnauthorizedException("Unauthorized: You do not have permission to delete this media.");
        }

        mediaRepository.delete(media);
    }
}
