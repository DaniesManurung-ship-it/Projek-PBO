package com.kldel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kldel.model.Promo;

import java.time.LocalDate;
import java.util.List;

public interface PromoRepository extends JpaRepository<Promo, Long> {
    @Query("SELECT p FROM Promo p WHERE p.active = true AND p.startDate <= :today AND p.endDate >= :today")
    List<Promo> findActivePromos(@Param("today") LocalDate today);
    
    List<Promo> findAllByOrderByCreatedAtDesc();
    long count();
}
