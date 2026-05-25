package com.kopitiam33.controller;

import com.kopitiam33.model.Notifikasi;
import com.kopitiam33.model.User;
import com.kopitiam33.repository.NotifikasiRepository;
import com.kopitiam33.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/notifikasi")
public class NotifikasiController {

    private final NotifikasiRepository notifikasiRepository;
    private final UserRepository userRepository;

    public NotifikasiController(NotifikasiRepository notifikasiRepository, UserRepository userRepository) {
        this.notifikasiRepository = notifikasiRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getNotifications(Authentication auth, Model model) {
        User user = null;
        if (auth != null && auth.isAuthenticated()) {
            user = userRepository.findByUsername(auth.getName()).orElse(null);
        }
        
        if (user != null) {
            List<Notifikasi> notifications = notifikasiRepository.findByUserOrderByCreatedAtDesc(user);
            model.addAttribute("notifications", notifications);
            notifikasiRepository.markAllAsRead(user);
        } else {
            model.addAttribute("notifications", List.of());
        }
        
        model.addAttribute("activePage", "notifikasi");
        return "notifikasi";
    }
}