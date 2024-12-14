package com.java2.ticketingsystembackend.controller;

import com.java2.ticketingsystembackend.dto.CreateEventDTO;
import com.java2.ticketingsystembackend.dto.EventDTO;
import com.java2.ticketingsystembackend.dto.UpdateEventDTO;
import com.java2.ticketingsystembackend.entity.Event;
import com.java2.ticketingsystembackend.exception.EntityNotFoundException;
import com.java2.ticketingsystembackend.exception.UnauthorizedException;
import com.java2.ticketingsystembackend.service.EventService;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public ResponseEntity<EventDTO> getEventInfo(@RequestParam Integer id) {
        try {
            Optional<EventDTO> eventDTO = eventService.getEventById(id);

            if (eventDTO.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return eventDTO.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/info/{uuid}")
    @PermitAll
    public ResponseEntity<EventDTO> getEventByUuid(@PathVariable String uuid) {
        return eventService.getEventByUuid(uuid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public ResponseEntity<String> createEvent(@RequestBody CreateEventDTO eventDTO) {
        try {
            Event event = eventService.createEvent(eventDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(event.getUuid());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/update/{uuid}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable String uuid, @RequestBody UpdateEventDTO updatedEvent) {
        try {
            EventDTO updateResult = eventService.updateEvent(uuid, updatedEvent);
            return ResponseEntity.ok(updateResult);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/delete/{uuid}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public ResponseEntity<String> deleteEvent(@PathVariable String uuid) {
        try {
            eventService.deleteEvent(uuid);
            return ResponseEntity.status(HttpStatus.OK).body("Event with UUID " + uuid + " was deleted.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found with UUID " + uuid);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error, please check log");
        }
    }
}

