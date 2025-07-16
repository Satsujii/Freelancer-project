package com.freelance.marketplace.config;

import com.freelance.marketplace.entity.Role;
import com.freelance.marketplace.entity.User;
import com.freelance.marketplace.repository.UserRepository;
import com.freelance.marketplace.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.annotation.PostConstruct;

@Configuration
public class AdminUserInitializer {
    private static final String ADMIN_EMAIL = "admin@admin.com";
    private static final String ADMIN_PASSWORD = "admin123456";
    private static final String ADMIN_NAME = "Admin";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminService adminService;

    @PostConstruct
    public void ensureAdminUserExists() {
        if (!userRepository.existsByEmail(ADMIN_EMAIL)) {
            User admin = new User();
            admin.setName(ADMIN_NAME);
            admin.setEmail(ADMIN_EMAIL);
            admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);
            admin = userRepository.save(admin);
            adminService.createAdminProfileIfNotExists(admin);
            System.out.println("Default admin user created: " + ADMIN_EMAIL + " / " + ADMIN_PASSWORD);
        } else {
            User admin = userRepository.findByEmail(ADMIN_EMAIL).get();
            // Always update the password to the new default
            admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
            userRepository.save(admin);
            adminService.createAdminProfileIfNotExists(admin);
            System.out.println("Default admin user password updated: " + ADMIN_EMAIL + " / " + ADMIN_PASSWORD);
        }
    }
} 