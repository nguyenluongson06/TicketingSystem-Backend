package com.java2.ticketingsystembackend.service;

import com.java2.ticketingsystembackend.dto.EventDTO;
import com.java2.ticketingsystembackend.exception.UnauthorizedException;
import com.java2.ticketingsystembackend.mapper.EventMapper;
import com.java2.ticketingsystembackend.security.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import com.java2.ticketingsystembackend.entity.*;
import com.java2.ticketingsystembackend.repository.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationUtils authenticationUtils;

    public List<EventDTO> getEventListByUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getAuthorities().isEmpty() || auth.getPrincipal().equals("anonymousUser")) {
            System.out.println("Anonymous user is querying event list");
            return eventRepository.findByIsPublicTrue().stream()
                    .map(EventMapper::toEventDTO)
                    .collect(Collectors.toList());
        }

        Object principal = auth.getPrincipal(); //principal is userName
        User user = userRepository.findByUsername(String.valueOf(principal)).get();

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


    public Optional<EventDTO> getEventById(Integer eventId) {
        ///check auth then get user
        User user = authenticationUtils.getAuthenticatedUser();

        ///try to find event
        Optional<Event> eventOpt = eventRepository.findById(eventId);

        if (eventOpt.isEmpty()) {
            return Optional.empty();
        }

        Event event = eventOpt.get();

        // Role-based access control
        if (user.getRole().getName().equals("ADMIN")) {
            // Admins can access any event
            return Optional.of(EventMapper.toEventDTO(event));
        } else if (user.getRole().getName().equals("ORGANIZER")) {
            // Organizers can only access events they organize
            if (event.getOrganizer().getId().equals(user.getId())) {
                return Optional.of(EventMapper.toEventDTO(event));
            } else {
                throw new UnauthorizedException("Unauthorized: You do not have permission to access this resource.");
            }
        }

        // All others (normal users) get 401
        throw new UnauthorizedException("Unauthorized: You do not have permission to access this resource.");
    }

    public Optional<EventDTO> getEventByUuid(String uuid) {
        ///find event first, if event is public just return it
        Optional<Event> eventOpt = eventRepository.findByUuid(uuid);
        if (eventOpt.isEmpty()) {
            return Optional.empty();
        }
        Event event = eventOpt.get();
        if (event.getIsPublic()){
            return Optional.of(EventMapper.toEventDTO(event));
        }

        ///check auth = get user
        User user = authenticationUtils.getAuthenticatedUser();

        ///if admin or organizer own the event then return event
        if (user.getRole().getName().equals("ADMIN") || (user.getRole().getName().equals("ORGANIZER") && event.getOrganizer().getId().equals(user.getId()))) return Optional.of(EventMapper.toEventDTO(event));

        // All other cases get 401
        throw new UnauthorizedException("Unauthorized: You do not have permission to access this resource.");
    }

    public Event createEvent(Event event) {
        ///auth + get user
        User user = authenticationUtils.getAuthenticatedUser();

        ///only create event if user is admin/organizer
        if (user.getRole().getName().equals("ADMIN") || user.getRole().getName().equals("ORGANIZER")) {
            event.setOrganizer(user);
            return eventRepository.save(event);
        }
        throw new UnauthorizedException("Unauthorized: You do not have permission to access this resource.");
    }

    public Event updateEvent(Integer eventId, Event updatedEvent) {
        User user = authenticationUtils.getAuthenticatedUser();

        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        /// Check if user has permission: admin can change any event, organizer can change event they own
        if ( user.getRole().getName().equals("ADMIN") || (user.getRole().getName().equals("ORGANIZER")) && (existingEvent.getOrganizer().getId().equals(user.getId()))) {
            updatedEvent.setId(eventId);
            return eventRepository.save(updatedEvent);
        }
        throw new UnauthorizedException("Unauthorized: You do not have permission to access this resource.");
    }

    public void deleteEvent(Integer eventId) {
        User user = authenticationUtils.getAuthenticatedUser();

        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        /// Check if user has permission: admin can delete any event, organizer can delete event they own
        if (user.getRole().getName().equals("ADMIN") || (user.getRole().getName().equals("ORGANIZER")) && (existingEvent.getOrganizer().getId().equals(user.getId()))) eventRepository.delete(existingEvent);

        throw new UnauthorizedException("Unauthorized: You do not have permission to access this resource.");
    }
}
