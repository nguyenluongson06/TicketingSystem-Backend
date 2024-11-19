package com.java2.ticketingsystembackend.repository;

import com.java2.ticketingsystembackend.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Integer> {
    List<Media> findByEvent_Id(Integer eventId);
}


