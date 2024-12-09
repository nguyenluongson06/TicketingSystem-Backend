package com.java2.ticketingsystembackend.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.java2.ticketingsystembackend.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendReservationEmail(String to, String username, String eventName, String qrCodeLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // Enable multipart

            helper.setTo(to);
            helper.setSubject("Reservation Confirmed!");
            helper.setFrom("your-email@gmail.com");

            // Generate QR code image
            byte[] qrCodeImage = generateQRCode(qrCodeLink);

            // Create the HTML body with inline QR code
            String body = String.format(
                    """
                    <html>
                        <body>
                            <p>Dear %s,</p>
                            <p>Your reservation for the event <strong>'%s'</strong> has been successfully confirmed.</p>
                            <p>Please find your QR code below for check-in:</p>
                            <img src='cid:qrCodeImage' alt='QR Code' style='width:200px;height:200px;'/>
                            <p>Alternatively, you can use this link to check in: <a href='%s'>%s</a></p>
                            <p>Best regards,<br>TickVN Team</p>
                        </body>
                    </html>
                    """, username, eventName, qrCodeLink, qrCodeLink);

            helper.setText(body, true); // Enable HTML content

            // Add QR code as inline content
            helper.addInline("qrCodeImage", new ByteArrayResource(qrCodeImage), "image/png");

            // Send the email
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send reservation email with inline QR code", e);
        }
    }

    public void sendRegistrationEmail(User user) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // Enable multipart

            helper.setTo(user.getEmail());
            helper.setSubject("Registration Confirmed!");

            // Create the HTML body with inline QR code
            String body = String.format(
                    """
                    <html>
                        <body>
                            <p>Dear %s,</p>
                            <p>You've successfully registered an account with us.</p>
                            <p>We hope you have a pleasant experience.</p>
                            <p>Best regards,<br>TickVN Team</p>
                        </body>
                    </html>
                    """, user.getFullname());

            helper.setText(body, true); // Enable HTML content

            // Send the email
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send registration email", e);
        }
    }


    private byte[] generateQRCode(String link) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(link, BarcodeFormat.QR_CODE, 250, 250);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }
}

