package com.java2.ticketingsystembackend.service;

import com.java2.ticketingsystembackend.entity.*;
import com.java2.ticketingsystembackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getEvents(User user) {
        if (user.getRole().getName().equals("ADMIN")) {
            return eventRepository.findAll();
        } else if (user.getRole().getName().equals("ORGANIZER")) {
            return eventRepository.findAll().stream()
                    .filter(e -> e.getOrganizer().getId() == user.getId() || e.getIsPublic())
                    .collect(Collectors.toList());
        } else {
            return eventRepository.findAll().stream()
                    .filter(Event::getIsPublic)
                    .collect(Collectors.toList());
        }
    }

    public Optional<Event> getEventDetail(String uuid, User user) throws Exception {
        Optional<Event> event = eventRepository.findByUuid(uuid);
        if (event.isPresent() && (event.get().getIsPublic() || event.get().getOrganizer().equals(user))) {
            return event;
        }
        throw new Exception("You do not have access to this event.");
    }
}

