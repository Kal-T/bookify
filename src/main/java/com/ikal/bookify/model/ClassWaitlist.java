package com.ikal.bookify.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "class_waitlist")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassWaitlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waitlistId;

    private Long userId;
    private Long classId;

    @CreationTimestamp
    private LocalDateTime addedAt;

    public ClassWaitlist(Long userId, Long classId, LocalDateTime addedAt) {
        this.userId = userId;
        this.classId = classId;
        this.addedAt = addedAt;
    }
}
