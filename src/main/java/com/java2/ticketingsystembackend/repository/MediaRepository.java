package com.java2.ticketingsystembackend.repository;

import com.java2.ticketingsystembackend.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Integer> {
    List<Media> findAllByEventId(int eventId);
}
