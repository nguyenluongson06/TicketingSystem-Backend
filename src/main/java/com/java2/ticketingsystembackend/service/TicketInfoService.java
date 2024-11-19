package com.java2.ticketingsystembackend.service;

import com.java2.ticketingsystembackend.entity.TicketInfo;
import com.java2.ticketingsystembackend.repository.TicketInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketInfoService {

    private final TicketInfoRepository ticketInfoRepository;

    @Autowired
    public TicketInfoService(TicketInfoRepository ticketInfoRepository) {
        this.ticketInfoRepository = ticketInfoRepository;
    }

    public TicketInfo getTicketInfoById(Integer id) {
        return ticketInfoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket Info not found"));
    }

    public TicketInfo updateTicketInfo(Integer id, TicketInfo updatedInfo) {
        TicketInfo existingInfo = ticketInfoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket Info not found"));

        existingInfo.setTicketName(updatedInfo.getTicketName());
        existingInfo.setTicketType(updatedInfo.getTicketType());
        existingInfo.setPosition(updatedInfo.getPosition());
        existingInfo.setMaxQuantity(updatedInfo.getMaxQuantity());
        existingInfo.setPrice(updatedInfo.getPrice());

        return ticketInfoRepository.save(existingInfo);
    }

    public void deleteTicketInfo(Integer id) {
        TicketInfo ticketInfo = ticketInfoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket Info not found"));
        ticketInfoRepository.delete(ticketInfo);
    }
}

