package com.ABCEnglish.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Cấu hình phân quyền
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Tắt CSRF
                .authorizeHttpRequests(authorize -> authorize
                        // Các API công cộng
                        .requestMatchers("/api/v1/users").permitAll()
                        .requestMatchers("/api/v1/verify").permitAll()
                        .requestMatchers("/api/v1/auth/token").permitAll()
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/course/add").permitAll()
                        .requestMatchers("/api/v1/course/updated/**").permitAll()

                        // Các API chỉ dành cho người dùng có vai trò ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Các API yêu cầu người dùng đã đăng nhập
                        .anyRequest().permitAll()// Mặc định yêu cầu đăng nhập cho tất cả các API còn lại
                );
        return http.build(); // Kết thúc cấu hình và trả về SecurityFilterChain
    }

    // Cấu hình UserDetailsService (dùng dữ liệu mẫu)
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // Tạo người dùng mẫu để thử nghiệm
            if ("admin".equals(username)) {
                return User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode("admin123")) // Mật khẩu đã mã hóa
                        .roles("ADMIN") // Gán vai trò ADMIN
                        .build();
            } else if ("user".equals(username)) {
                return User.builder()
                        .username("user")
                        .password(passwordEncoder().encode("user123")) // Mật khẩu đã mã hóa
                        .roles("USER") // Gán vai trò USER
                        .build();
            }
            throw new RuntimeException("User not found");
        };
    }
}
