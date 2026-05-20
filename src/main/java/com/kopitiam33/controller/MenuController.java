package com.kopitiam33.controller;

import com.kopitiam33.model.Menu;
import com.kopitiam33.repository.MenuRepository;
import com.kopitiam33.service.ImageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {

    private final MenuRepository menuRepository;
    private final ImageService imageService;

    public MenuController(MenuRepository menuRepository, ImageService imageService) {
        this.menuRepository = menuRepository;
        this.imageService = imageService;
    }

    @GetMapping
    public String getAllMenu(Model model) {
        List<Menu> menus = menuRepository.findAll();
        model.addAttribute("menus", menus);
        model.addAttribute("activePage", "menu");
        return "menu";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Menu getMenu(@PathVariable Long id) {
        return menuRepository.findById(id).orElse(null);
    }
}