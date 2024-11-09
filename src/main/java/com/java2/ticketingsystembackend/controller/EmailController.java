package com.java2.ticketingsystembackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.java2.ticketingsystembackend.service.EmailService;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;
}

