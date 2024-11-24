package com.java2.ticketingsystembackend.service;

import com.java2.ticketingsystembackend.dto.*;
import com.java2.ticketingsystembackend.exception.EntityNotFoundException;
import com.java2.ticketingsystembackend.exception.UnauthorizedException;
import com.java2.ticketingsystembackend.mapper.EventMapper;
import com.java2.ticketingsystembackend.security.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.java2.ticketingsystembackend.entity.*;
import com.java2.ticketingsystembackend.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationUtils authenticationUtils;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<EventDTO> getEventListByUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getAuthorities().isEmpty() || auth.getPrincipal().equals("anonymousUser")) {
            System.out.println("Anonymous user is querying event list");
            return eventRepository.findByIsPublicTrue().stream()
                    .map(EventMapper::toEventDTO)
                    .collect(Collectors.toList());
        }

        Object principal = auth.getPrincipal(); //principal is userName
        Optional<User> userOpt = userRepository.findByUsername(String.valueOf(principal));
        if (userOpt.isEmpty()){
            System.out.println("Anonymous user is querying event list");
            return eventRepository.findByIsPublicTrue().stream()
                    .map(EventMapper::toEventDTO)
                    .collect(Collectors.toList());
        }

        User user = userOpt.get();

        // Access role using Authentication authorities
        if (user.getRole().getName().equals("ADMIN")) {
            System.out.println("Admin user is querying event list");
            return eventRepository.findAll().stream()
                    .map(EventMapper::toEventDTO)
                    .collect(Collectors.toList()); // Admins get all events
        } else if (user.getRole().getName().equals("ORGANIZER")) {
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

    public Event createEvent(CreateEventDTO eventDTO) {
        // Authenticate and get the current user
        User user = authenticationUtils.getAuthenticatedUser();

        // Check if the user has the necessary role
        if (!user.getRole().getName().equals("ADMIN") && !user.getRole().getName().equals("ORGANIZER")) {
            throw new UnauthorizedException("Unauthorized: You do not have permission to access this resource.");
        }

        // Validate required fields in the DTO
        if (eventDTO.getName() == null || eventDTO.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (eventDTO.getPlace() == null || eventDTO.getPlace().isBlank()) {
            throw new IllegalArgumentException("Place is required.");
        }
        if (eventDTO.getMaxQuantity() == null) {
            throw new IllegalArgumentException("MaxQuantity is required.");
        }
        if (eventDTO.getIsPublic() == null) {
            throw new IllegalArgumentException("isPublic is required.");
        }
        if (eventDTO.getCategoryId() == null) {
            throw new IllegalArgumentException("CategoryId is required.");
        }

        // Create a new Event entity and populate fields
        Event event = new Event();
        event.setUuid(UUID.randomUUID().toString()); // Generate a new UUID
        event.setName(eventDTO.getName());
        event.setTimeStart(eventDTO.getTimeStart() != null ? eventDTO.getTimeStart() : LocalDateTime.now());
        event.setTimeEnd(eventDTO.getTimeEnd() != null ? eventDTO.getTimeEnd() : LocalDateTime.now());
        event.setPlace(eventDTO.getPlace());
        event.setDescription(eventDTO.getDescription() != null ? eventDTO.getDescription() : "Event description is empty");
        event.setMaxQuantity(eventDTO.getMaxQuantity());
        event.setIsPublic(eventDTO.getIsPublic());
        event.setCategory(categoryRepository.findById(eventDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found."))); // Fetch and set category
        event.setOrganizer(user); // Set the current user as the organizer

        // Save the event and return the created entity
        return eventRepository.save(event);
    }

    public EventDTO updateEvent(String uuid, UpdateEventDTO eventDTO) {
        // Authenticate and get the current user
        User user = authenticationUtils.getAuthenticatedUser();

        // Check if the user has the necessary role
        if (!user.getRole().getName().equals("ADMIN") && !user.getRole().getName().equals("ORGANIZER")) {
            throw new UnauthorizedException("Unauthorized: You do not have permission to access this resource.");
        }

        // Fetch the existing event
        Event existingEvent = eventRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        // Check if the user has permission:
        // Admins can update any event; organizers can update events they own
        if (!user.getRole().getName().equals("ADMIN") &&
                !(user.getRole().getName().equals("ORGANIZER") && existingEvent.getOrganizer().getId().equals(user.getId()))) {
            throw new UnauthorizedException("Unauthorized: You do not have permission to update this resource.");
        }

        // Update fields only if they are provided in the DTO
        if (eventDTO.getName() != null) {
            existingEvent.setName(eventDTO.getName());
        }
        if (eventDTO.getTimeStart() != null) {
            existingEvent.setTimeStart(eventDTO.getTimeStart());
        }
        if (eventDTO.getTimeEnd() != null) {
            existingEvent.setTimeEnd(eventDTO.getTimeEnd());
        }
        if (eventDTO.getPlace() != null) {
            existingEvent.setPlace(eventDTO.getPlace());
        }
        if (eventDTO.getDescription() != null) {
            existingEvent.setDescription(eventDTO.getDescription());
        }
        if (eventDTO.getMaxQuantity() != null) {
            existingEvent.setMaxQuantity(eventDTO.getMaxQuantity());
        }
        if (eventDTO.getIsPublic() != null) {
            existingEvent.setIsPublic(eventDTO.getIsPublic());
        }
        if (eventDTO.getCategoryId() != null) {
            existingEvent.setCategory(categoryRepository.findById(eventDTO.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found")));
        }

        // Ensure the current user is still set as the organizer
        // remove, in cases that admin update the event info, but different organizer
        // existingEvent.setOrganizer(user);

        // Save the updated event
        eventRepository.save(existingEvent);
        return EventMapper.toEventDTO(existingEvent);
    }


    public void deleteEvent(String uuid) {
        User user = authenticationUtils.getAuthenticatedUser();

        Event existingEvent = eventRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        /// Check if user has permission: admin can delete any event, organizer can delete event they own
        if (user.getRole().getName().equals("ADMIN") || (user.getRole().getName().equals("ORGANIZER")) && (existingEvent.getOrganizer().getId().equals(user.getId()))) {
            eventRepository.delete(existingEvent);
        } else {
            throw new UnauthorizedException("Unauthorized: You do not have permission to access this resource.");
        }
    }
}
