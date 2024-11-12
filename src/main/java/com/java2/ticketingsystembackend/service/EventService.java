package com.java2.ticketingsystembackend.service;

import com.java2.ticketingsystembackend.entity.*;
import com.java2.ticketingsystembackend.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    ///TODO: auth + chia role
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    ///TODO: auth + chia response theo role
    public Optional<Event> getEventById(int id) {
        return eventRepository.findById(id);
    }

    ///TODO: auth + chia role
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    ///TODO: auth + chia role
    public void deleteEvent(int id) {
        eventRepository.deleteById(id);
    }
}

