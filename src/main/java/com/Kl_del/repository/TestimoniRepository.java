package com.Kl_del.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Kl_del.model.Testimoni;
import com.Kl_del.model.User;

import java.util.List;

@Repository
public interface TestimoniRepository extends JpaRepository<Testimoni, Long> {
    
    // Untuk customer: melihat testimoni milik sendiri
    List<Testimoni> findByUserOrderByCreatedAtDesc(User user);
    
    // Untuk SEMUA customer: melihat testimoni yang sudah DISETUJUI dan BELUM DIARSIPKAN
    List<Testimoni> findByApprovedTrueAndArchivedFalseOrderByCreatedAtDesc();
    
    // Untuk admin: melihat semua testimoni
    List<Testimoni> findAllByOrderByCreatedAtDesc();
    
    // Menghitung total testimoni
    long count();
    
}