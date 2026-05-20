package com.kopitiam33.controller;

import com.kopitiam33.model.Promo;
import com.kopitiam33.repository.PromoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/promo")
public class PromoController {

    private final PromoRepository promoRepository;

    public PromoController(PromoRepository promoRepository) {
        this.promoRepository = promoRepository;
    }

    @GetMapping
    public String getAllPromo(Model model) {
        List<Promo> promos = promoRepository.findAllByOrderByCreatedAtDesc();
        model.addAttribute("promos", promos);
        model.addAttribute("activePage", "promo");
        return "promo";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Promo getPromo(@PathVariable Long id) {
        return promoRepository.findById(id).orElse(null);
    }
}