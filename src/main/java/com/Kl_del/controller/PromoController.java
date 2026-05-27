package com.Kl_del.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.Kl_del.model.Promo;
import com.Kl_del.repository.PromoRepository;

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