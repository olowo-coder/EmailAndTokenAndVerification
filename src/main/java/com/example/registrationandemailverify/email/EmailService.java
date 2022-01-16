package com.example.registrationandemailverify.email;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService implements EmailSender{
    private final JavaMailSender mailSender;

    @Override
    @Async
    public void send(String to, String email) {
        try {
            MimeMessage mimeMailMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, "utf-8");

            helper.setFrom("lanre.olowo@yahoo.com");
            helper.setTo(to);
            helper.setText(email, true);
            helper.setSubject("Confirm your email");

            mailSender.send(mimeMailMessage);

        } catch (MessagingException e) {
            log.error("failed to send mail", e);
            throw new IllegalStateException("failed to send");
        }
    }
}
