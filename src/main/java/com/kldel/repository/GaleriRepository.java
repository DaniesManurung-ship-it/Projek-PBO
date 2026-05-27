package com.kldel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kldel.model.Galeri;

import java.util.List;

public interface GaleriRepository extends JpaRepository<Galeri, Long> {
    List<Galeri> findByActiveTrueOrderByCreatedAtDesc();
    List<Galeri> findAllByOrderByCreatedAtDesc();
    long count();
}
