package com.Kl_del.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.Kl_del.model.Galeri;
import com.Kl_del.repository.GaleriRepository;

import java.util.List;

@Controller
@RequestMapping("/galeri")
public class GaleriController {

    private final GaleriRepository galeriRepository;

    public GaleriController(GaleriRepository galeriRepository) {
        this.galeriRepository = galeriRepository;
    }

    @GetMapping
    public String getAllGaleri(Model model) {
        List<Galeri> galleries = galeriRepository.findByActiveTrueOrderByCreatedAtDesc();
        model.addAttribute("galleries", galleries);
        model.addAttribute("activePage", "galeri");
        return "galeri";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Galeri getGaleri(@PathVariable Long id) {
        return galeriRepository.findById(id).orElse(null);
    }
}