package com.bitirmeprojesi.ecommerce.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationOtpMail(String userEmail, String otp, String subject, String text) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text);
            mimeMessageHelper.setTo(userEmail);

            mailSender.send(mimeMessage);
        } catch (MailSendException e) {
            System.out.println("ERROR  : " + e.getMessage());
            throw new MailSendException("failed to send verification OTP mail", e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
