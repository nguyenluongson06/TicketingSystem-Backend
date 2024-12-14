package com.java2.ticketingsystembackend.controller;

import com.java2.ticketingsystembackend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> sendTestEmail(@RequestParam String to) {
        try {
            emailService.sendEmail(to, "Test Email", "This is a test email from the email service.");
            return ResponseEntity.ok("Test email sent successfully to " + to);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/testqr")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> sendTestEmailWithQr(@RequestParam String to, @RequestParam String qrCodeLink) {
        try {
            emailService.sendReservationEmail(to, "testUserName", "testEventName", qrCodeLink);
            return ResponseEntity.ok("Test email with QR sent successfully to " + to);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

