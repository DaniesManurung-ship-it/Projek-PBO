package com.kopitiam33.controller;

import com.kopitiam33.model.Pesanan;
import com.kopitiam33.model.User;
import com.kopitiam33.repository.PesananRepository;
import com.kopitiam33.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/pesanan")
public class PesananController {

    private final PesananRepository pesananRepository;
    private final UserRepository userRepository;

    public PesananController(PesananRepository pesananRepository, UserRepository userRepository) {
        this.pesananRepository = pesananRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/add")
    public String addPesanan(Authentication auth,
                             @RequestParam String customerName,
                                
                             @RequestParam(required = false) String notes,
                             @RequestParam String cartData,
                             RedirectAttributes redirectAttributes) {
        
        try {
            User user = null;
            if (auth != null && auth.isAuthenticated()) {
                user = userRepository.findByUsername(auth.getName()).orElse(null);
            }
            
            // Parse cartData and calculate total
            ObjectMapper mapper = new ObjectMapper();
            JsonNode items = mapper.readTree(cartData);
            int totalPrice = 0;
            for (JsonNode item : items) {
                int price = item.get("price").asInt();
                int quantity = item.get("quantity").asInt();
                totalPrice += price * quantity;
            }
            
            Pesanan pesanan = new Pesanan();
            pesanan.setUser(user);
            pesanan.setCustomerName(customerName);
         
            pesanan.setNotes(notes);
            pesanan.setItems(cartData);
            pesanan.setTotalPrice(totalPrice);
            pesanan.setStatus("pending");
            
            pesananRepository.save(pesanan);
            
            // Redirect ke halaman cart dengan parameter success
            redirectAttributes.addFlashAttribute("success", "Pesanan berhasil dikirim! Silakan tunggu konfirmasi.");
            return "redirect:/cart?success=true";
            
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Gagal mengirim pesanan: " + e.getMessage());
            return "redirect:/cart?error=true";
        }
    }
    
    // ==================== LIHAT RIWAYAT PESANAN CUSTOMER ====================
    @GetMapping("/riwayat")
    public String riwayatPesanan(Authentication auth, Model model) {
        User currentUser = null;
        List<Pesanan> pesanans = null;
        
        if (auth != null && auth.isAuthenticated()) {
            currentUser = userRepository.findByUsername(auth.getName()).orElse(null);
        }
        
        if (currentUser != null) {
            pesanans = pesananRepository.findByUserOrderByCreatedAtDesc(currentUser);
        } else {
            pesanans = List.of();
        }
        
        model.addAttribute("pesanans", pesanans);
        model.addAttribute("activePage", "riwayat");
        return "riwayat";
    }
    
    // ==================== DETAIL PESANAN ====================
    @GetMapping("/detail/{id}")
    @ResponseBody
    public Pesanan getDetailPesanan(@PathVariable Long id) {
        return pesananRepository.findById(id).orElse(null);
    }
    
    // ==================== BATAL PESANAN ====================
    @GetMapping("/batal/{id}")
    public String batalPesanan(@PathVariable Long id, Authentication auth, RedirectAttributes redirectAttributes) {
        Pesanan pesanan = pesananRepository.findById(id).orElse(null);
        User currentUser = null;
        
        if (auth != null && auth.isAuthenticated()) {
            currentUser = userRepository.findByUsername(auth.getName()).orElse(null);
        }
        
        if (pesanan != null && currentUser != null && pesanan.getUser() != null && pesanan.getUser().getId().equals(currentUser.getId())) {
            if ("pending".equals(pesanan.getStatus())) {
                pesanan.setStatus("cancelled");
                pesananRepository.save(pesanan);
                redirectAttributes.addFlashAttribute("success", "Pesanan berhasil dibatalkan!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Pesanan tidak dapat dibatalkan karena sudah " + pesanan.getStatus());
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Gagal membatalkan pesanan!");
        }
        
        return "redirect:/pesanan/riwayat";
    }
}