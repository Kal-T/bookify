package com.ikal.bookify.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "class_bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    private Long userId;
    private Long classId;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private Integer creditsDeducted;

    @CreationTimestamp
    private LocalDateTime bookedAt;

    private LocalDateTime canceledAt;
    private LocalDateTime checkInTime;

    @Enumerated(EnumType.STRING)
    private CheckInStatus checkInStatus = CheckInStatus.PENDING;

    public enum BookingStatus {
        BOOKED, CANCELED, WAITLIST
    }

    public enum CheckInStatus {
        PENDING, CHECKED_IN, MISSED
    }

    public ClassBooking(Long userId, Long classId, BookingStatus bookingStatus, Integer creditsDeducted, LocalDateTime bookedAt, LocalDateTime canceledAt, LocalDateTime checkInTime, CheckInStatus checkInStatus) {
        this.userId = userId;
        this.classId = classId;
        this.bookingStatus = bookingStatus;
        this.creditsDeducted = creditsDeducted;
        this.bookedAt = bookedAt;
        this.canceledAt = canceledAt;
        this.checkInTime = checkInTime;
        this.checkInStatus = checkInStatus;
    }


}

