package com.ikal.bookify.dto;

import com.ikal.bookify.model.Package;
import com.ikal.bookify.model.User;
import com.ikal.bookify.model.UserPackage;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class UserPackageResponse {

    private Long userPackageId;


    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package aPackage;

    private int remainingCredits;

    @Enumerated(EnumType.STRING)
    private UserPackage.Status status;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    public enum Status {
        ACTIVE, EXPIRED
    }
}
