package com.kopitiam33.controller;

import com.kopitiam33.model.*;
import com.kopitiam33.repository.*;
import com.kopitiam33.service.ImageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final MenuRepository menuRepository;
    private final PromoRepository promoRepository;
    private final TestimoniRepository testimoniRepository;
    private final GaleriRepository galeriRepository;
    private final ReservasiRepository reservasiRepository;
    private final PesananRepository pesananRepository;
    private final ImageService imageService;

    public AdminController(MenuRepository menuRepository,
                         PromoRepository promoRepository,
                         TestimoniRepository testimoniRepository,
                         GaleriRepository galeriRepository,
                         ReservasiRepository reservasiRepository,
                         PesananRepository pesananRepository,
                         ImageService imageService) {
        this.menuRepository = menuRepository;
        this.promoRepository = promoRepository;
        this.testimoniRepository = testimoniRepository;
        this.galeriRepository = galeriRepository;
        this.reservasiRepository = reservasiRepository;
        this.pesananRepository = pesananRepository;
        this.imageService = imageService;
    }

    @GetMapping({"", "/"})
    public String dashboard(Model model) {
        model.addAttribute("menuCount", menuRepository.count());
        model.addAttribute("reservasiCount", reservasiRepository.count());
        model.addAttribute("testimoniCount", testimoniRepository.count());
        model.addAttribute("promoCount", promoRepository.count());
        model.addAttribute("pesananCount", pesananRepository.count());

        List<Reservasi> recentReservasi = reservasiRepository.findAllByOrderByCreatedAtDesc().stream().limit(5).toList();
        model.addAttribute("recentReservasi", recentReservasi);

        List<Testimoni> recentTestimoni = testimoniRepository.findAllByOrderByCreatedAtDesc().stream().limit(5).toList();
        model.addAttribute("recentTestimoni", recentTestimoni);

        return "admin/dashboard";
    }

    // ==================== MENU CRUD ====================
    @GetMapping("/menu")
    public String manageMenu(Model model) {
        model.addAttribute("menus", menuRepository.findAll());
        return "admin/menu";
    }

    @PostMapping("/menu/add")
    public String addMenu(@RequestParam String name,
                         @RequestParam(required = false) String description,
                         @RequestParam Integer price,
                         @RequestParam String category,
                         @RequestParam(required = false) String badge,
                         @RequestParam(required = false) MultipartFile image,
                         RedirectAttributes redirectAttributes) {
        try {
            Menu menu = new Menu();
            menu.setName(name);
            menu.setDescription(description);
            menu.setPrice(price);
            menu.setCategory(category);
            menu.setBadge(badge);
            menu.setAvailable(true);
            
            if (image != null && !image.isEmpty()) {
                String imagePath = imageService.saveImage(image);
                menu.setImage(imagePath);
            }
            
            menuRepository.save(menu);
            redirectAttributes.addFlashAttribute("success", "Menu berhasil ditambahkan!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menambahkan menu: " + e.getMessage());
        }
        return "redirect:/admin/menu";
    }

    @PostMapping("/menu/update")
    public String updateMenu(@RequestParam Long id,
                            @RequestParam String name,
                            @RequestParam(required = false) String description,
                            @RequestParam Integer price,
                            @RequestParam String category,
                            @RequestParam(required = false) String badge,
                            @RequestParam(required = false) MultipartFile image,
                            RedirectAttributes redirectAttributes) {
        try {
            Menu menu = menuRepository.findById(id).orElse(null);
            if (menu != null) {
                menu.setName(name);
                menu.setDescription(description);
                menu.setPrice(price);
                menu.setCategory(category);
                menu.setBadge(badge);
                
                if (image != null && !image.isEmpty()) {
                    String imagePath = imageService.saveImage(image);
                    menu.setImage(imagePath);
                }
                
                menuRepository.save(menu);
                redirectAttributes.addFlashAttribute("updated", "Menu berhasil diupdate!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Menu tidak ditemukan!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal mengupdate menu: " + e.getMessage());
        }
        return "redirect:/admin/menu";
    }

    @GetMapping("/menu/delete/{id}")
    public String deleteMenu(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            menuRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("deleted", "Menu berhasil dihapus!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menghapus menu: " + e.getMessage());
        }
        return "redirect:/admin/menu";
    }

    @PostMapping("/menu/toggle/{id}")
    @ResponseBody
    public String toggleMenu(@PathVariable Long id) {
        Menu menu = menuRepository.findById(id).orElse(null);
        if (menu != null) {
            menu.setAvailable(!menu.isAvailable());
            menuRepository.save(menu);
            return "success";
        }
        return "error";
    }

    @GetMapping("/menu/{id}")
    @ResponseBody
    public Menu getMenu(@PathVariable Long id) {
        return menuRepository.findById(id).orElse(null);
    }

    // ==================== PROMO CRUD ====================
    @GetMapping("/promo")
    public String managePromo(Model model) {
        model.addAttribute("promos", promoRepository.findAllByOrderByCreatedAtDesc());
        return "admin/promo";
    }

    @PostMapping("/promo/add")
    public String addPromo(@RequestParam String name,
                          @RequestParam(required = false) String description,
                          @RequestParam Integer originalPrice,
                          @RequestParam Integer discount,
                          @RequestParam String startDate,
                          @RequestParam String endDate,
                          @RequestParam(required = false) MultipartFile image,
                          RedirectAttributes redirectAttributes) {
        try {
            Promo promo = new Promo();
            promo.setName(name);
            promo.setDescription(description);
            promo.setOriginalPrice(originalPrice);
            promo.setDiscount(discount);
            promo.setStartDate(LocalDate.parse(startDate));
            promo.setEndDate(LocalDate.parse(endDate));
            promo.setActive(true);
            
            if (image != null && !image.isEmpty()) {
                promo.setImage(imageService.saveImage(image));
            }
            
            promoRepository.save(promo);
            redirectAttributes.addFlashAttribute("success", "Promo berhasil ditambahkan!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menambahkan promo: " + e.getMessage());
        }
        return "redirect:/admin/promo";
    }

    @PostMapping("/promo/update")
    public String updatePromo(@RequestParam Long id,
                             @RequestParam String name,
                             @RequestParam(required = false) String description,
                             @RequestParam Integer originalPrice,
                             @RequestParam Integer discount,
                             @RequestParam String startDate,
                             @RequestParam String endDate,
                             @RequestParam(required = false) MultipartFile image,
                             RedirectAttributes redirectAttributes) {
        try {
            Promo promo = promoRepository.findById(id).orElse(null);
            if (promo != null) {
                promo.setName(name);
                promo.setDescription(description);
                promo.setOriginalPrice(originalPrice);
                promo.setDiscount(discount);
                promo.setStartDate(LocalDate.parse(startDate));
                promo.setEndDate(LocalDate.parse(endDate));
                
                if (image != null && !image.isEmpty()) {
                    promo.setImage(imageService.saveImage(image));
                }
                
                promoRepository.save(promo);
                redirectAttributes.addFlashAttribute("updated", "Promo berhasil diupdate!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Promo tidak ditemukan!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal mengupdate promo: " + e.getMessage());
        }
        return "redirect:/admin/promo";
    }

    @GetMapping("/promo/delete/{id}")
    public String deletePromo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            promoRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("deleted", "Promo berhasil dihapus!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menghapus promo: " + e.getMessage());
        }
        return "redirect:/admin/promo";
    }

    @PostMapping("/promo/toggle/{id}")
    @ResponseBody
    public String togglePromo(@PathVariable Long id) {
        Promo promo = promoRepository.findById(id).orElse(null);
        if (promo != null) {
            promo.setActive(!promo.isActive());
            promoRepository.save(promo);
            return "success";
        }
        return "error";
    }

    @GetMapping("/promo/{id}")
    @ResponseBody
    public Promo getPromo(@PathVariable Long id) {
        return promoRepository.findById(id).orElse(null);
    }

    // ==================== GALERI CRUD ====================
    @GetMapping("/galeri")
    public String manageGaleri(Model model) {
        model.addAttribute("galeris", galeriRepository.findAllByOrderByCreatedAtDesc());
        return "admin/galeri";
    }

    @PostMapping("/galeri/add")
    public String addGaleri(@RequestParam String title,
                           @RequestParam(required = false) String description,
                           @RequestParam String category,
                           @RequestParam MultipartFile image,
                           RedirectAttributes redirectAttributes) {
        try {
            Galeri galeri = new Galeri();
            galeri.setTitle(title);
            galeri.setDescription(description);
            galeri.setCategory(category);
            galeri.setActive(true);
            
            if (image != null && !image.isEmpty()) {
                galeri.setImage(imageService.saveImage(image));
            }
            
            galeriRepository.save(galeri);
            redirectAttributes.addFlashAttribute("success", "Galeri berhasil ditambahkan!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menambahkan galeri: " + e.getMessage());
        }
        return "redirect:/admin/galeri";
    }

    @PostMapping("/galeri/update")
    public String updateGaleri(@RequestParam Long id,
                              @RequestParam String title,
                              @RequestParam(required = false) String description,
                              @RequestParam String category,
                              @RequestParam(required = false) MultipartFile image,
                              RedirectAttributes redirectAttributes) {
        try {
            Galeri galeri = galeriRepository.findById(id).orElse(null);
            if (galeri != null) {
                galeri.setTitle(title);
                galeri.setDescription(description);
                galeri.setCategory(category);
                
                if (image != null && !image.isEmpty()) {
                    galeri.setImage(imageService.saveImage(image));
                }
                
                galeriRepository.save(galeri);
                redirectAttributes.addFlashAttribute("updated", "Galeri berhasil diupdate!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Galeri tidak ditemukan!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal mengupdate galeri: " + e.getMessage());
        }
        return "redirect:/admin/galeri";
    }

    @GetMapping("/galeri/delete/{id}")
    public String deleteGaleri(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            galeriRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("deleted", "Galeri berhasil dihapus!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menghapus galeri: " + e.getMessage());
        }
        return "redirect:/admin/galeri";
    }

    @PostMapping("/galeri/toggle/{id}")
    @ResponseBody
    public String toggleGaleri(@PathVariable Long id) {
        Galeri galeri = galeriRepository.findById(id).orElse(null);
        if (galeri != null) {
            galeri.setActive(!galeri.isActive());
            galeriRepository.save(galeri);
            return "success";
        }
        return "error";
    }

    @GetMapping("/galeri/{id}")
    @ResponseBody
    public Galeri getGaleri(@PathVariable Long id) {
        return galeriRepository.findById(id).orElse(null);
    }

    // ==================== RESERVASI ====================
    @GetMapping("/reservasi")
    public String viewReservasi(Model model) {
        model.addAttribute("reservasis", reservasiRepository.findAllByOrderByCreatedAtDesc());
        return "admin/reservasi";
    }

    @PostMapping("/reservasi/status/{id}")
    @ResponseBody
    public String updateReservasiStatus(@PathVariable Long id, @RequestParam String status) {
        Reservasi reservasi = reservasiRepository.findById(id).orElse(null);
        if (reservasi != null) {
            reservasi.setStatus(status);
            if (status.equals("confirmed") || status.equals("cancelled") || status.equals("completed")) {
                reservasi.setCanEdit(false);
            }
            reservasiRepository.save(reservasi);
            return "success";
        }
        return "error";
    }

    @GetMapping("/reservasi/{id}")
    @ResponseBody
    public Reservasi getReservasi(@PathVariable Long id) {
        return reservasiRepository.findById(id).orElse(null);
    }

    @GetMapping("/reservasi/delete/{id}")
    public String deleteReservasi(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reservasiRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("deleted", "Reservasi berhasil dihapus!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menghapus reservasi: " + e.getMessage());
        }
        return "redirect:/admin/reservasi";
    }

    // ==================== TESTIMONI ====================
    @GetMapping("/testimoni")
    public String manageTestimoni(Model model) {
        model.addAttribute("testimonis", testimoniRepository.findAllByOrderByCreatedAtDesc());
        return "admin/testimoni";
    }

    @PostMapping("/testimoni/approve/{id}")
    @ResponseBody
    public String approveTestimoni(@PathVariable Long id) {
        Testimoni testimoni = testimoniRepository.findById(id).orElse(null);
        if (testimoni != null) {
            testimoni.setApproved(true);
            testimoniRepository.save(testimoni);
            return "success";
        }
        return "error";
    }

    @PostMapping("/testimoni/unapprove/{id}")
    @ResponseBody
    public String unapproveTestimoni(@PathVariable Long id) {
        Testimoni testimoni = testimoniRepository.findById(id).orElse(null);
        if (testimoni != null) {
            testimoni.setApproved(false);
            testimoniRepository.save(testimoni);
            return "success";
        }
        return "error";
    }

    @PostMapping("/testimoni/archive/{id}")
    @ResponseBody
    public String archiveTestimoni(@PathVariable Long id) {
        Testimoni testimoni = testimoniRepository.findById(id).orElse(null);
        if (testimoni != null) {
            testimoni.setArchived(true);
            testimoniRepository.save(testimoni);
            return "success";
        }
        return "error";
    }

    @GetMapping("/testimoni/delete/{id}")
    public String deleteTestimoni(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            testimoniRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("deleted", "Testimoni berhasil dihapus!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menghapus testimoni: " + e.getMessage());
        }
        return "redirect:/admin/testimoni";
    }

    // ==================== PESANAN (ORDER) ====================
    @GetMapping("/pesanan")
    public String viewPesanan(Model model) {
        model.addAttribute("pesanans", pesananRepository.findAllByOrderByCreatedAtDesc());
        return "admin/pesanan";
    }

    @PostMapping("/pesanan/status/{id}")
    @ResponseBody
    public String updatePesananStatus(@PathVariable Long id, @RequestParam String status) {
        Pesanan pesanan = pesananRepository.findById(id).orElse(null);
        if (pesanan != null) {
            pesanan.setStatus(status);
            pesananRepository.save(pesanan);
            return "success";
        }
        return "error";
    }

    @GetMapping("/pesanan/delete/{id}")
    public String deletePesanan(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            pesananRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("deleted", "Pesanan berhasil dihapus!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal menghapus pesanan: " + e.getMessage());
        }
        return "redirect:/admin/pesanan";
    }

    @GetMapping("/pesanan/{id}")
    @ResponseBody
    public Pesanan getPesanan(@PathVariable Long id) {
        return pesananRepository.findById(id).orElse(null);
    }
    
}