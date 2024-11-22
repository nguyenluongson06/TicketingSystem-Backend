package com.java2.ticketingsystembackend.repository;

import com.java2.ticketingsystembackend.entity.TicketInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketInfoRepository extends JpaRepository<TicketInfo, Integer> {
}

