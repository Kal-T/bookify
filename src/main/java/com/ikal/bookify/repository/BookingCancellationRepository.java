package com.ikal.bookify.repository;

import com.ikal.bookify.model.BookingCancellations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingCancellationRepository extends JpaRepository<BookingCancellations, Long> { }
