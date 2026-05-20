package com.kopitiam33.controller;

import com.kopitiam33.model.Reservasi;
import com.kopitiam33.model.User;
import com.kopitiam33.repository.ReservasiRepository;
import com.kopitiam33.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/reservasi")
public class ReservasiController {

    private final ReservasiRepository reservasiRepository;
    private final UserRepository userRepository;

    public ReservasiController(ReservasiRepository reservasiRepository, UserRepository userRepository) {
        this.reservasiRepository = reservasiRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getAllReservasi(Authentication auth, Model model) {
        User currentUser = userRepository.findByUsername(auth.getName()).orElse(null);
        
        if (currentUser != null && "ADMIN".equals(currentUser.getRole())) {
            List<Reservasi> reservasis = reservasiRepository.findAllByOrderByCreatedAtDesc();
            model.addAttribute("reservasis", reservasis);
        } else if (currentUser != null) {
            List<Reservasi> reservasis = reservasiRepository.findByUserOrderByCreatedAtDesc(currentUser);
            model.addAttribute("reservasis", reservasis);
        }
        
        model.addAttribute("activePage", "reservasi");
        return "reservasi";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Reservasi getReservasi(@PathVariable Long id) {
        return reservasiRepository.findById(id).orElse(null);
    }

    @PostMapping("/add")
    public String addReservasi(Authentication auth,
                              @RequestParam String name,
                              @RequestParam String email,
                              @RequestParam String phone,
                              @RequestParam String date,
                              @RequestParam String time,
                              @RequestParam Integer people,
                              @RequestParam(required = false) String notes,
                              RedirectAttributes redirectAttributes) {
        
        User user = userRepository.findByUsername(auth.getName()).orElse(null);
        
        Reservasi reservasi = new Reservasi();
        reservasi.setUser(user);
        reservasi.setName(name);
        reservasi.setEmail(email);
        reservasi.setPhone(phone);
        reservasi.setDate(LocalDate.parse(date));
        reservasi.setTime(time);
        reservasi.setPeople(people);
        reservasi.setNotes(notes);
        reservasi.setStatus("pending");
        reservasi.setCanEdit(true);
        
        reservasiRepository.save(reservasi);
        redirectAttributes.addFlashAttribute("success", "Reservasi berhasil dibuat!");
        return "redirect:/reservasi";
    }

    @PostMapping("/update/{id}")
    public String updateReservasi(@PathVariable Long id,
                                 @RequestParam String name,
                                 @RequestParam String email,
                                 @RequestParam String phone,
                                 @RequestParam String date,
                                 @RequestParam String time,
                                 @RequestParam Integer people,
                                 @RequestParam(required = false) String notes,
                                 RedirectAttributes redirectAttributes) {
        
        Reservasi reservasi = reservasiRepository.findById(id).orElse(null);
        if (reservasi != null && reservasi.isCanEdit()) {
            reservasi.setName(name);
            reservasi.setEmail(email);
            reservasi.setPhone(phone);
            reservasi.setDate(LocalDate.parse(date));
            reservasi.setTime(time);
            reservasi.setPeople(people);
            reservasi.setNotes(notes);
            reservasiRepository.save(reservasi);
            redirectAttributes.addFlashAttribute("success", "Reservasi berhasil diupdate!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Reservasi tidak dapat diedit!");
        }
        
        return "redirect:/reservasi";
    }

    @PostMapping("/delete/{id}")
    public String deleteReservasi(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Reservasi reservasi = reservasiRepository.findById(id).orElse(null);
        if (reservasi != null && reservasi.isCanEdit()) {
            reservasiRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Reservasi berhasil dibatalkan!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Reservasi tidak dapat dibatalkan!");
        }
        return "redirect:/reservasi";
    }
}