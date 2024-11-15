package com.ikal.bookify.repository;

import com.ikal.bookify.model.ClassWaitlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaitlistRepository extends JpaRepository<ClassWaitlist, Long> {
    List<ClassWaitlist> findByClassIdOrderByAddedAtAsc(Long classId);

    List<ClassWaitlist> findByClassId(Long classId);

    Optional<ClassWaitlist> findByUserIdAndClassId(Long userId, Long classId);
}
