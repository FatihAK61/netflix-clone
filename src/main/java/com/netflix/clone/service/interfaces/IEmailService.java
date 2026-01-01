package com.netflix.clone.service.interfaces;

public interface IEmailService {

    void sendVerificationEmail(String toEmail, String token);

    void sendPasswordResetEmail(String toEmail, String token);
    
}
