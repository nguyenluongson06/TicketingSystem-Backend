package com.java2.ticketingsystembackend.controller;

import com.java2.ticketingsystembackend.dto.EventDTO;
import com.java2.ticketingsystembackend.entity.Event;
import com.java2.ticketingsystembackend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/list")
    public ResponseEntity<List<EventDTO>> getEventList() {
        List<EventDTO> events = eventService.getEventListByUserRole();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/info")
    public ResponseEntity<EventDTO> getEventInfo(@RequestParam Integer id) {
        /*
        Optional<Event> eventOptional = eventService.getEventById(id);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            EventDTO result = new EventDTO(event.getId(), event.getUuid(), event.getName(), event.getTimeStart(), event.getTimeEnd(), event.getPlace(), event.getDescription(), event.getMaxQuantity(), event.getIsPublic(), new OrganizerDTO(event.getOrganizer().getUuid(), event.getOrganizer().getFullname()), event.getCategory().getName());
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
        */

        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @GetMapping("/info/{uuid}")
    public ResponseEntity<EventDTO> getEventByUuid(@PathVariable String uuid) {
        return eventService.getEventByUuid(uuid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event createdEvent = eventService.createEvent(event);
        return ResponseEntity.ok(createdEvent);
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public ResponseEntity<Event> updateEvent(@PathVariable Integer eventId, @RequestBody Event updatedEvent) {
        Event event = eventService.updateEvent(eventId, updatedEvent);
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}

