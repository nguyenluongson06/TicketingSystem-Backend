package com.java2.ticketingsystembackend.service;

import com.java2.ticketingsystembackend.dto.EventDTO;
import com.java2.ticketingsystembackend.mapper.EventMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.java2.ticketingsystembackend.entity.*;
import com.java2.ticketingsystembackend.repository.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    public List<EventDTO> getEventsByUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getAuthorities().isEmpty() || auth.getPrincipal().equals("anonymousUser")) {
            System.out.println("Anonymous user is querying event list");
            return eventRepository.findByIsPublicTrue().stream()
                    .map(EventMapper::toEventDTO)
                    .collect(Collectors.toList());
        }

        Object principal = auth.getPrincipal(); //principal is userName
        User user = userRepository.findByUsername(String.valueOf(principal)).get();
        System.out.print("Current user authorities:");
        auth.getAuthorities().stream().forEach(a -> System.out.print(a + " "));
        System.out.println();
        /*
        if (principal instanceof UserDetails){
            //handle if principal is an instance of UserDetails
            String userName = ((UserDetails)principal).getUsername();
            user = userRepository.findByUsername(userName).get();
        } else if ((principal instanceof User)) {
            //directly cast principal to User if its an instance of User
            user = (User) principal;
        } else {
            System.out.println("Principal is not of type User or UserDetails");
            throw new IllegalStateException("Principal is not a valid User/UserdDetails object");
        }*/

        // Access role using Authentication authorities
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            System.out.println("Admin user is querying event list");
            return eventRepository.findAll().stream()
                    .map(EventMapper::toEventDTO)
                    .collect(Collectors.toList()); // Admins get all events
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ORGANIZER"))) {
            System.out.println("Organizer user is querying event list");
            return eventRepository.findByOrganizerId(user.getId()).stream()
                    .map(EventMapper::toEventDTO)
                    .collect(Collectors.toList()); // Organizer-specific events
        } else {
            System.out.println("Normal user is querying event list");
            return eventRepository.findByIsPublicTrue().stream()
                    .map(EventMapper::toEventDTO)
                    .collect(Collectors.toList()); // Public events for normal users
        }
    }


    public Optional<Event> getEventById(Integer eventId) {
        return eventRepository.findById(eventId);
    }

    public Optional<Event> getEventByUuid(String uuid) {
        return eventRepository.findByUuid(uuid);
    }

    public Event createEvent(Event event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        // Only admins and organizers can create events
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) ||
                auth.getAuthorities().contains(new SimpleGrantedAuthority("ORGANIZER"))) {
            event.setOrganizer(userRepository.findById(user.getId()).get());
            return eventRepository.save(event);
        }
        throw new RuntimeException("Unauthorized");
    }

    public Event updateEvent(Integer eventId, Event updatedEvent) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Check if user has permission
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) ||
                (auth.getAuthorities().contains(new SimpleGrantedAuthority("ORGANIZER")) &&
                        existingEvent.getOrganizer().getId().equals(user.getId()))) {
            updatedEvent.setId(eventId);
            return eventRepository.save(updatedEvent);
        }
        throw new RuntimeException("Unauthorized");
    }

    public void deleteEvent(Integer eventId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Check if user has permission
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) ||
                (auth.getAuthorities().contains(new SimpleGrantedAuthority("ORGANIZER")) &&
                        existingEvent.getOrganizer().getId().equals(user.getId()))) {
            eventRepository.delete(existingEvent);
        } else {
            throw new RuntimeException("Unauthorized");
        }
    }
}
