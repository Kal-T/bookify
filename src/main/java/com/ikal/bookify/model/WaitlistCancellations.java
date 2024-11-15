package com.ikal.bookify.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "waitlist_cancellations")
@Data
public class WaitlistCancellations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cancellationId;

    private Long waitlistId;

    @CreationTimestamp
    private LocalDateTime canceledAt;

    @Enumerated(EnumType.STRING)
    private RefundStatus refundStatus;

    private Integer refundAmount;

    public enum RefundStatus {
        PENDING, REFUNDED, NOT_REFUNDED
    }
}

