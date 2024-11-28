package com.ABCEnglish.configuration;

import com.ABCEnglish.entity.Role;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.reponsesitory.RoleRepository;
import com.ABCEnglish.reponsesitory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
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
        // Kiểm tra vai trò ADMIN đã tồn tại chưa
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ADMIN");
                    return roleRepository.save(role);
                });

        // Kiểm tra tài khoản admin đã tồn tại chưa
        if (userRepository.findByPhone("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setFullname("Administrator");
            admin.setEmail("admin@example.com");
            admin.setPhone("admin");
            admin.setPassword(passwordEncoder.encode("password123")); // Mật khẩu mã hóa
            admin.setRole(adminRole);
            admin.setStatus(true); // Trạng thái hoạt động
            admin.setCreatedAt(new Date());
            admin.setUpdatedAt(new Date());
            userRepository.save(admin);
            System.out.println("Admin account created successfully.");
        } else {
            System.out.println("Admin account already exists.");
        }
    }
}
