package com.ikal.bookify.repository;

import com.ikal.bookify.model.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {

    // Find classes by country
    List<Class> findByCountry(String country);

    // Find available classes for a specific country
    @Query("SELECT c FROM Class c WHERE c.country = :country AND c.availableSlots > 0 AND c.scheduleTime > :currentTime")
    List<Class> findAvailableClasses(@Param("country") String country, @Param("currentTime") LocalDateTime currentTime);
}

