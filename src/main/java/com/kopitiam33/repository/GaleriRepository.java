package com.kopitiam33.repository;

import com.kopitiam33.model.Galeri;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GaleriRepository extends JpaRepository<Galeri, Long> {
    List<Galeri> findByActiveTrueOrderByCreatedAtDesc();
    List<Galeri> findAllByOrderByCreatedAtDesc();
    long count();
}