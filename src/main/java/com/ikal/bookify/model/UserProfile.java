package com.ikal.bookify.model;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userProfileId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String avatarUrl;
    private String contactNumber;

    // Getters and setters
}