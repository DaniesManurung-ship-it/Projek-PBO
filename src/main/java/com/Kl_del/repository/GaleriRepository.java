package com.Kl_del.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Kl_del.model.Galeri;

import java.util.List;

public interface GaleriRepository extends JpaRepository<Galeri, Long> {
    List<Galeri> findByActiveTrueOrderByCreatedAtDesc();
    List<Galeri> findAllByOrderByCreatedAtDesc();
    long count();
}