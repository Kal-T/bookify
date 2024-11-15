package com.ikal.bookify.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "packages")
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;

    private String name;
    private String country;
    private int credits;
    private BigDecimal price;
    private LocalDate expiryDate;

}

