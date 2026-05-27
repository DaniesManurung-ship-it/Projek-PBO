package com.kldel;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.kldel.model.User;
import com.kldel.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("========== DATA INITIALIZER START ==========");
        
        // Create admin user if not exists
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@kldel.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Administrator");
            admin.setRole("ADMIN");
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println("✅ Admin user created!");
            System.out.println("   Username: admin");
            System.out.println("   Password: admin123");
            System.out.println("   Password hash: " + admin.getPassword());
        } else {
            System.out.println("Admin user already exists");
            User existing = userRepository.findByUsername("admin").get();
            System.out.println("Existing password hash: " + existing.getPassword());
        }

        // Create test customer if not exists
        if (userRepository.findByUsername("customer").isEmpty()) {
            User customer = new User();
            customer.setUsername("customer");
            customer.setEmail("customer@kldel.com");
            customer.setPassword(passwordEncoder.encode("customer123"));
            customer.setFullName("Test Customer");
            customer.setRole("USER");
            customer.setActive(true);
            userRepository.save(customer);
            System.out.println("✅ Customer user created!");
            System.out.println("   Username: customer");
            System.out.println("   Password: customer123");
        }
        
        System.out.println("========== DATA INITIALIZER END ==========");
    }
}
