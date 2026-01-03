package com.netflix.clone.service.impl;

import com.netflix.clone.helper.globalexceptionhandler.EmailNotVerifiedException;
import com.netflix.clone.service.interfaces.IEmailService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${app.front.url:http://localhost:4200}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendVerificationEmail(String toEmail, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Netflix Clone - Email Verification");

            String emailBody = getVerificationString(token);
            message.setText(emailBody);
            mailSender.send(message);
            logger.info("Verification email sent to {}", toEmail);
        } catch (Exception e) {
            logger.error("Error sending verification email to {}: {}", toEmail, e.getLocalizedMessage());
            throw new EmailNotVerifiedException(("Error sending verification email to " + toEmail));
        }
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Netflix Clone - Password Reset");

            String emailBody = getResetString(token);
            message.setText(emailBody);
            mailSender.send(message);
            logger.info("Password reset email sent to {}", toEmail);
        } catch (Exception e) {
            logger.error("Error sending password reset email to {}: {}", toEmail, e.getLocalizedMessage());
            throw new RuntimeException(("Error sending password reset email to " + toEmail));
        }
    }

    private @NonNull String getResetString(String token) {
        String resetLink = frontendUrl + "/reset-password?token=" + token;
        return "Hi,\n\n"
                + "We received a request to reset your password. Please click on the link below to reset your password:\n\n"
                + resetLink
                + "\n\n"
                + "If you did not request this, please ignore this email.\n\n"
                + "Best regards,\n"
                + "Netflix Clone Team";
    }

    private @NonNull String getVerificationString(String token) {
        String verificationUrl = frontendUrl + "/verify-email?token=" + token;

        return "Welcome to Netflix Clone!\n\n"
                + "Thanks for registering! Please verify your email by clicking on the link below:\n\n"
                + verificationUrl
                + "\n\n"
                + "This link will expire in 24 hours.\n\n"
                + "If you did not create this account, please ignore this email.\n\n"
                + "Best regards,\n"
                + "Netflix Clone Team";
    }

}
