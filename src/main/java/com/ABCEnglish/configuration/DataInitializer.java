package com.ABCEnglish.configuration;

import com.ABCEnglish.entity.Role;
import com.ABCEnglish.entity.User;

import com.ABCEnglish.repository.RoleRepository;
import com.ABCEnglish.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ADMIN");
                    return roleRepository.save(role);
                });

        if (userRepository.findByPhone("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setFullname("Administrator");
            admin.setEmail("admin@example.com");
            admin.setPhone("admin");
            admin.setPassword(passwordEncoder.encode("password123"));
            admin.setRole(adminRole);
            admin.setStatus(true);
            admin.setCreatedAt(new Date());
            admin.setUpdatedAt(new Date());
            userRepository.save(admin);
            System.out.println(" Admin account created successfully.");
        } else {
            System.out.println(" Admin account already exists.");
        }
    }
}