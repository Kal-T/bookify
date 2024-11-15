package com.ikal.bookify.repository;

import com.ikal.bookify.model.BookingCancellations;
import com.ikal.bookify.model.WaitlistCancellations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitlistCancellationRepository extends JpaRepository<WaitlistCancellations, Long> { }
