package com.java2.ticketingsystembackend.repository;

import com.java2.ticketingsystembackend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {}

