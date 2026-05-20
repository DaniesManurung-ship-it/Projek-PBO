package com.kopitiam33.controller;

import com.kopitiam33.model.User;
import com.kopitiam33.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String viewProfile(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", loggedUser);
        model.addAttribute("activePage", "profile");
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(@RequestParam String fullName,
                                @RequestParam(required = false) String phone,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }
        
        loggedUser.setFullName(fullName);
        if (phone != null) {
            loggedUser.setPhone(phone);
        }
        userRepository.save(loggedUser);
        session.setAttribute("loggedUser", loggedUser);
        
        redirectAttributes.addFlashAttribute("success", "Profile berhasil diupdate!");
        return "redirect:/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }
        
        // Cek password saat ini
        if (!passwordEncoder.matches(currentPassword, loggedUser.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Password saat ini salah!");
            return "redirect:/profile";
        }
        
        // Cek konfirmasi password baru
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Konfirmasi password tidak sesuai!");
            return "redirect:/profile";
        }
        
        // Cek panjang password minimal
        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "Password baru minimal 6 karakter!");
            return "redirect:/profile";
        }
        
        // Update password
        loggedUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(loggedUser);
        
        redirectAttributes.addFlashAttribute("success", "Password berhasil diubah!");
        return "redirect:/profile";
    }
}