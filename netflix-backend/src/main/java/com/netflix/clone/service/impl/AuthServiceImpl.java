package com.netflix.clone.service.impl;

import com.netflix.clone.dto.request.UserRequest;
import com.netflix.clone.dto.response.EmailValidationResponse;
import com.netflix.clone.dto.response.LoginResponse;
import com.netflix.clone.dto.response.MessageResponse;
import com.netflix.clone.helper.enums.Roles;
import com.netflix.clone.helper.globalexceptionhandler.*;
import com.netflix.clone.models.User;
import com.netflix.clone.repository.UserRepository;
import com.netflix.clone.service.interfaces.IAuthService;
import com.netflix.clone.service.interfaces.IEmailService;
import com.netflix.clone.utils.jwt.JwtUtil;
import com.netflix.clone.utils.service.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IEmailService emailService;
    private final JwtUtil jwtUtil;
    private final ServiceUtils serviceUtils;

    @Override
    public MessageResponse signup(UserRequest userRequest) {

        if (userRepository.existsByEmail(userRequest.getEmail()))
            throw new EmailAlreadyExistsException("Email already exists!");

        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setFullName(userRequest.getFullName());
        user.setRole(Roles.USER);
        user.setActive(true);
        user.setEmailVerified(false);
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setVerificationTokenExpiry(Instant.now().plusSeconds(86400));
        userRepository.save(user);
        emailService.sendVerificationEmail(user.getEmail(), verificationToken);
        return new MessageResponse("Registration successful! Please check your email for verification link.");
    }

    @Override
    public LoginResponse login(String email, String password) {
        User user = userRepository
                .findByEmail(email)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials!"));

        if (!user.isActive())
            throw new AccountDeactivatedException("Your account has been deactivated! Please contact support team.");

        if (!user.isEmailVerified())
            throw new EmailNotVerifiedException("Please verify your email first!");

        final String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new LoginResponse(token, user.getEmail(), user.getFullName(), user.getRole().name());
    }

    @Override
    public EmailValidationResponse validateEmail(String email) {
        boolean exists = userRepository.existsByEmail(email);
        return new EmailValidationResponse(exists, !exists);
    }

    @Override
    public MessageResponse verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired verification token!"));

        if (user.getVerificationTokenExpiry() == null || user.getVerificationTokenExpiry().isBefore(Instant.now()))
            throw new InvalidTokenException("Invalid or expired verification token!");

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);
        userRepository.save(user);
        return new MessageResponse("Email verified successfully!");
    }

    @Override
    public MessageResponse resendVerification(String email) {
        User user = serviceUtils.getUserByEmailOrThrow(email);

        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setVerificationTokenExpiry(Instant.now().plusSeconds(86400));
        userRepository.save(user);
        emailService.sendVerificationEmail(email, verificationToken);
        return new MessageResponse("Verification email resent successfully!");
    }

    @Override
    public MessageResponse forgotPassword(String email) {
        User user = serviceUtils.getUserByEmailOrThrow(email);

        String resetToken = UUID.randomUUID().toString();
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetTokenExpiry(Instant.now().plusSeconds(3600));
        userRepository.save(user);
        emailService.sendPasswordResetEmail(email, resetToken);
        return new MessageResponse("Password reset email sent successfully!");
    }

    @Override
    public MessageResponse resetPassword(String token, String password) {
        User user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired password reset token!"));

        if (user.getPasswordResetTokenExpiry() == null || user.getPasswordResetTokenExpiry().isBefore(Instant.now()))
            throw new InvalidTokenException("Invalid or expired password reset token!");

        user.setPassword(passwordEncoder.encode(password));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiry(null);
        userRepository.save(user);
        return new MessageResponse("Password reset successfully!");
    }

    @Override
    public MessageResponse changePassword(String email, String currentPassword, String newPassword) {
        User user = serviceUtils.getUserByEmailOrThrow(email);

        if (!passwordEncoder.matches(currentPassword, user.getPassword()))
            throw new InvalidCredentialsException("Invalid current password!");

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return new MessageResponse("Password changed successfully!");
    }

    @Override
    public LoginResponse currentUser(String email) {
        User user = serviceUtils.getUserByEmailOrThrow(email);
        return new LoginResponse(null, user.getEmail(), user.getFullName(), user.getRole().name());
    }

}
