package com.kopitiam33.repository;

import com.kopitiam33.model.Pesanan;
import com.kopitiam33.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PesananRepository extends JpaRepository<Pesanan, Long> {
    List<Pesanan> findByUserOrderByCreatedAtDesc(User user);
    List<Pesanan> findAllByOrderByCreatedAtDesc();
    long count();
}