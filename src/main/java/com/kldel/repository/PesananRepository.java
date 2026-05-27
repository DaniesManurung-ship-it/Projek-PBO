package com.kldel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kldel.model.Pesanan;
import com.kldel.model.User;

import java.util.List;

public interface PesananRepository extends JpaRepository<Pesanan, Long> {
    
    // Untuk customer: melihat pesanan milik sendiri
    List<Pesanan> findByUserOrderByCreatedAtDesc(User user);
    
    // Untuk admin: melihat semua pesanan
    List<Pesanan> findAllByOrderByCreatedAtDesc();
    
    // Menghitung total pesanan
    long count();
    
    // ==================== UNTUK STATISTIK CHART ====================
    @Query(value = "SELECT COUNT(*) FROM pesanans WHERE MONTH(created_at) = :month AND YEAR(created_at) = :year", nativeQuery = true)
    long countByMonthAndYear(@Param("month") int month, @Param("year") int year);
    
    // Mendapatkan semua pesanan
    List<Pesanan> findAll();
}
