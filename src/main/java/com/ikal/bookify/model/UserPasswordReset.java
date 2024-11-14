package com.ikal.bookify.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_password_resets")
public class UserPasswordReset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resetId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String resetToken;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;

    // Getters and setters
}
