package com.java2.ticketingsystembackend.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Cannot send email" + e.getMessage());
        }

    }
}
