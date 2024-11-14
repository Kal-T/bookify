package com.ikal.bookify.repository;

import com.ikal.bookify.model.UserPasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPasswordResetRepository extends JpaRepository<UserPasswordReset, Long> {
    Optional<UserPasswordReset> findByResetToken(String token);
}
