package com.kopitiam33.controller;

import com.kopitiam33.model.*;
import com.kopitiam33.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class HomeController {

    private final MenuRepository menuRepository;
    private final PromoRepository promoRepository;
    private final TestimoniRepository testimoniRepository;
    private final GaleriRepository galeriRepository;
    private final ReservasiRepository reservasiRepository;

    public HomeController(MenuRepository menuRepository, 
                         PromoRepository promoRepository,
                         TestimoniRepository testimoniRepository,
                         GaleriRepository galeriRepository,
                         ReservasiRepository reservasiRepository) {
        this.menuRepository = menuRepository;
        this.promoRepository = promoRepository;
        this.testimoniRepository = testimoniRepository;
        this.galeriRepository = galeriRepository;
        this.reservasiRepository = reservasiRepository;
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        // Statistics
        model.addAttribute("menuCount", menuRepository.count());
        model.addAttribute("reservasiCount", reservasiRepository.count());
        model.addAttribute("testimoniCount", testimoniRepository.count());
        model.addAttribute("promoCount", promoRepository.count());
        
        // Active Promos
        LocalDate today = LocalDate.now();
        List<Promo> activePromos = promoRepository.findActivePromos(today);
        model.addAttribute("activePromos", activePromos.stream().limit(3).toList());
        
        // Approved Testimonials (Hanya yang sudah disetujui admin)
        List<Testimoni> testimonials = testimoniRepository.findByApprovedTrueAndArchivedFalseOrderByCreatedAtDesc();
        model.addAttribute("testimonials", testimonials.stream().limit(6).toList());
        
        // Gallery
        List<Galeri> galleries = galeriRepository.findByActiveTrueOrderByCreatedAtDesc();
        model.addAttribute("galleries", galleries.stream().limit(8).toList());
        
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser != null) {
            model.addAttribute("username", loggedUser.getFullName());
        }
        
        model.addAttribute("activePage", "home");
        return "home";
    }
    
    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }
}