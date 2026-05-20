package com.kopitiam33.repository;

import com.kopitiam33.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByAvailableTrue();
    List<Menu> findByCategory(String category);
    long countByAvailableTrue();
}