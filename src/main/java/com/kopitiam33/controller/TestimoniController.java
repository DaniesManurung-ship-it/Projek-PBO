package com.kopitiam33.controller;

import com.kopitiam33.model.Testimoni;
import com.kopitiam33.model.User;
import com.kopitiam33.repository.TestimoniRepository;
import com.kopitiam33.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/testimoni")
public class TestimoniController {

    private final TestimoniRepository testimoniRepository;
    private final UserRepository userRepository;

    public TestimoniController(TestimoniRepository testimoniRepository, UserRepository userRepository) {
        this.testimoniRepository = testimoniRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getAllTestimoni(Authentication auth, Model model) {
        User currentUser = null;
        if (auth != null && auth.isAuthenticated()) {
            currentUser = userRepository.findByUsername(auth.getName()).orElse(null);
        }
        
        // Untuk halaman testimoni, tampilkan semua testimoni yang sudah disetujui
        List<Testimoni> testimonials = testimoniRepository.findByApprovedTrueAndArchivedFalseOrderByCreatedAtDesc();
        model.addAttribute("testimonials", testimonials);
        model.addAttribute("activePage", "testimoni");
        return "testimoni";
    }

    @PostMapping("/add")
    public String addTestimoni(Authentication auth,
                              @RequestParam String name,
                              @RequestParam String message,
                              @RequestParam Integer rating,
                              RedirectAttributes redirectAttributes) {
        
        User user = null;
        if (auth != null && auth.isAuthenticated()) {
            user = userRepository.findByUsername(auth.getName()).orElse(null);
        }
        
        Testimoni testimoni = new Testimoni();
        testimoni.setUser(user);
        testimoni.setName(name);
        testimoni.setMessage(message);
        testimoni.setRating(rating);
        testimoni.setApproved(false);  // Menunggu approval admin
        testimoni.setArchived(false);
        
        testimoniRepository.save(testimoni);
        redirectAttributes.addFlashAttribute("success", "Testimoni berhasil ditambahkan! Menunggu persetujuan admin.");
        return "redirect:/testimoni";
    }

    @GetMapping("/delete/{id}")
    public String deleteTestimoni(@PathVariable Long id, Authentication auth, RedirectAttributes redirectAttributes) {
        User currentUser = null;
        if (auth != null && auth.isAuthenticated()) {
            currentUser = userRepository.findByUsername(auth.getName()).orElse(null);
        }
        
        Testimoni testimoni = testimoniRepository.findById(id).orElse(null);
        
        if (testimoni != null && currentUser != null) {
            if ("ADMIN".equals(currentUser.getRole()) || 
                (testimoni.getUser() != null && testimoni.getUser().getId().equals(currentUser.getId()))) {
                testimoniRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Testimoni berhasil dihapus!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Anda tidak memiliki akses!");
            }
        }
        return "redirect:/testimoni";
    }
}   