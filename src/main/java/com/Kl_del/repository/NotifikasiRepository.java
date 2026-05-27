package com.Kl_del.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.Kl_del.model.Notifikasi;
import com.Kl_del.model.User;

import java.util.List;

public interface NotifikasiRepository extends JpaRepository<Notifikasi, Long> {
    List<Notifikasi> findByUserOrderByCreatedAtDesc(User user);
    List<Notifikasi> findByUserAndIsReadOrderByCreatedAtDesc(User user, boolean isRead);
    long countByUserAndIsRead(User user, boolean isRead);
    
    @Modifying
    @Transactional
    @Query("UPDATE Notifikasi n SET n.isRead = true WHERE n.user = :user")
    void markAllAsRead(@Param("user") User user);
}
