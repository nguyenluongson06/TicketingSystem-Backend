package com.java2.ticketingsystembackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * PRIVATE Send email to given address
     * can only be called by relevant APIs, never directly exposed as a public API
     * ex: register, reserveTicket,... will call emailService.sendEmail
     * @param to address to send email to
     * @param subject subject of the email
     * @param body body of the email (can be plain text or HTML)
     */
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your-email@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
