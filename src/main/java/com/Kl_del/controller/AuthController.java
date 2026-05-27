package com.Kl_del.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Kl_del.model.User;
import com.Kl_del.repository.UserRepository;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Username atau password salah!");
        }
        if (logout != null) {
            model.addAttribute("message", "Anda telah logout!");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                          @RequestParam String email,
                          @RequestParam String password,
                          @RequestParam String fullName,
                          Model model) {
        
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "Username sudah terdaftar!");
            return "register";
        }
        
        if (userRepository.existsByEmail(email)) {
            model.addAttribute("error", "Email sudah terdaftar!");
            return "register";
        }
        
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setRole("USER");
        
        userRepository.save(user);
        model.addAttribute("success", "Registrasi berhasil! Silakan login.");
        return "login";
    }
}