package com.java2.ticketingsystembackend.repository;

import com.java2.ticketingsystembackend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByIsPublicTrue(); // For normal users
    List<Event> findByOrganizerId(Integer organizerId); // For organizers
    Optional<Event> findByUuid(String uuid);//find event using uuid
    List<Event> findAllByCategoryId(Integer categoryId);
}