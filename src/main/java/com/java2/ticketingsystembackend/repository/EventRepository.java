﻿package com.java2.ticketingsystembackend.repository;

import com.java2.ticketingsystembackend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    Optional<Event> findByUuid(String uuid);
}