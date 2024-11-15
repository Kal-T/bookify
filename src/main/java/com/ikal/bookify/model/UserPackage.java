package com.ikal.bookify.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_packages")
public class UserPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPackageId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package aPackage;

    private int remainingCredits;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    public enum Status {
        ACTIVE, EXPIRED
    }

}

