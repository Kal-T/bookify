package com.ikal.bookify.service;

import com.ikal.bookify.exception.UserNotFoundException;
import com.ikal.bookify.model.User;
import com.ikal.bookify.model.UserPasswordReset;
import com.ikal.bookify.repository.UserPasswordResetRepository;
import com.ikal.bookify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UserPasswordResetRepository passwordResetRepository;


    @Autowired
    private UserRepository userRepository;

    public String createPasswordResetToken(String email) {
        UserPasswordReset resetToken = new UserPasswordReset();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
        resetToken.setUser(user);
        resetToken.setResetToken(UUID.randomUUID().toString());
        resetToken.setCreatedAt(LocalDateTime.now());
        resetToken.setExpiredAt(LocalDateTime.now().plusHours(1)); // Token valid for 1 hour
        passwordResetRepository.save(resetToken);
        return resetToken.getResetToken();
    }

    public boolean validateResetToken(String token) {
        Optional<UserPasswordReset> resetToken = passwordResetRepository.findByResetToken(token);
        return resetToken.isPresent() && resetToken.get().getExpiredAt().isAfter(LocalDateTime.now());
    }

    public void updatePassword(String token, String newPassword) {
        Optional<UserPasswordReset> resetToken = passwordResetRepository.findByResetToken(token);
        if (resetToken.isPresent() && resetToken.get().getExpiredAt().isAfter(LocalDateTime.now())) {
            User user = resetToken.get().getUser();
            user.setPasswordHash(newPassword);  // Password should be encoded
            userRepository.save(user);
            passwordResetRepository.delete(resetToken.get());  // Remove used token
        } else {
            throw new IllegalArgumentException("Invalid or expired token");
        }
    }
}