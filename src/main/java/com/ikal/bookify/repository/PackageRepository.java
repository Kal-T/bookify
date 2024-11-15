package com.ikal.bookify.repository;

import com.ikal.bookify.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    @Query("SELECT p FROM Package p WHERE LOWER(p.country) = LOWER(:country)")
    List<Package> findByCountry(@Param("country") String country);
}

