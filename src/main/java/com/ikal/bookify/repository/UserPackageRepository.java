package com.ikal.bookify.repository;

import com.ikal.bookify.model.Package;
import com.ikal.bookify.model.User;
import com.ikal.bookify.model.UserPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPackageRepository extends JpaRepository<UserPackage, Long> {
    List<UserPackage> findByUserAndStatus(User user, UserPackage.Status status);

    @Query("SELECT up FROM UserPackage up WHERE up.user.userId = :userId")
    List<UserPackage> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT up FROM UserPackage up WHERE up.user.userId = :userId AND up.aPackage.packageId = :packageId")
    Optional<UserPackage> findUserIdAndPackageId(@Param("userId") Long userId, @Param("packageId") Long packageId);

    @Query(value = "SELECT * FROM user_packages up WHERE up.user_id = :userId AND up.status = 'ACTIVE' ORDER BY up.purchase_date DESC LIMIT 1", nativeQuery = true)
    Optional<UserPackage> findMostRecentActivePackageByUserId(@Param("userId") Long userId);

    @Query("SELECT up.aPackage FROM UserPackage up WHERE up.user.userId = :userId")
    List<Package> findPackagesByUserId(@Param("userId") Long userId);
}