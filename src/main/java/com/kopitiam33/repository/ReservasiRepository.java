package com.kopitiam33.repository;

import com.kopitiam33.model.Reservasi;
import com.kopitiam33.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservasiRepository extends JpaRepository<Reservasi, Long> {
    List<Reservasi> findByUserOrderByCreatedAtDesc(User user);
    List<Reservasi> findAllByOrderByCreatedAtDesc();
    long count();
}