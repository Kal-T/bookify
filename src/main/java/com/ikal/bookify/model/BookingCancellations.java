package com.ikal.bookify.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "booking_cancellations")
@Data
public class BookingCancellations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cancellationId;

    private Long bookingId;

    @CreationTimestamp
    private LocalDateTime canceledAt;

    @Enumerated(EnumType.STRING)
    private RefundStatus refundStatus;

    private Integer refundAmount;

    public enum RefundStatus {
        PENDING, REFUNDED, NOT_REFUNDED
    }
}

