package com.java2.ticketingsystembackend.controller;

import com.java2.ticketingsystembackend.entity.Event;
import com.java2.ticketingsystembackend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/list")
    public List<Event> getEventList(Authentication auth) {
        // Fetch list based on user role and organizerId (logic to be implemented)
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public Event getEventDetail(@PathVariable int id, Authentication auth) {
        // Implement visibility check for the requested event and return it
        return eventService.getEventById(id).orElse(null);
    }

    @PostMapping("/create")
    public Event createEvent(@RequestBody Event event) {
        return eventService.saveEvent(event);
    }

    @PutMapping("/{id}")
    public Event changeEventDetail(@PathVariable Long id, @RequestBody Event updatedEvent) {
        // Implement access control for organizer/admin only
        return eventService.saveEvent(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public String deleteEvent(@PathVariable int id) {
        // Implement access control for organizer/admin only
        eventService.deleteEvent(id);
        return "Event deleted successfully";
    }
}
