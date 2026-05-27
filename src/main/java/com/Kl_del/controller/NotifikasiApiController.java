package com.Kl_del.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Kl_del.model.Notifikasi;
import com.Kl_del.model.User;
import com.Kl_del.repository.NotifikasiRepository;
import com.Kl_del.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/notifikasi")
public class NotifikasiApiController {

    private final NotifikasiRepository notifikasiRepository;
    private final UserRepository userRepository;

    public NotifikasiApiController(NotifikasiRepository notifikasiRepository, UserRepository userRepository) {
        this.notifikasiRepository = notifikasiRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/count")
    public long getUnreadCount(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            User user = userRepository.findByUsername(auth.getName()).orElse(null);
            if (user != null) {
                return notifikasiRepository.countByUserAndIsRead(user, false);
            }
        }
        return 0;
    }

    @GetMapping
    public List<Notifikasi> getNotifications(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            User user = userRepository.findByUsername(auth.getName()).orElse(null);
            if (user != null) {
                return notifikasiRepository.findByUserOrderByCreatedAtDesc(user);
            }
        }
        return List.of();
    }

    @PostMapping("/mark-read")
    public ResponseEntity<String> markAllAsRead(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            User user = userRepository.findByUsername(auth.getName()).orElse(null);
            if (user != null) {
                notifikasiRepository.markAllAsRead(user);
                return ResponseEntity.ok("success");
            }
        }
        return ResponseEntity.status(401).body("error");
    }
}
