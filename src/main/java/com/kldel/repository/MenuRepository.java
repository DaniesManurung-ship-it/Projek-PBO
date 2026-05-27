package com.kldel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kldel.model.Menu;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByAvailableTrue();
    List<Menu> findByCategory(String category);
    long countByAvailableTrue();
}
