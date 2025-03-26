package com.ABCEnglish.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Value("${jwt.signerKey}")
    private String signerKey;



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtLoggingFilter jwtLoggingFilter) throws Exception {
        http
                // Cấu hình phân quyền
                .authorizeHttpRequests(authorize -> authorize
                        // Cho phép truy cập không cần xác thực
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/verify/{token}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/course").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/document/{courseId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/exercise/{lessonId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/question/{exerciseId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/answer/{questionId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/lesson/{courseId}").permitAll()
                        // Tất cả các request khác cần xác thực
                        .anyRequest().authenticated()
                )
                // Thêm JwtLoggingFilter trước UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtLoggingFilter, UsernamePasswordAuthenticationFilter.class)
                // Tắt CSRF và cấu hình CORS
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // Không dùng oauth2ResourceServer vì JwtLoggingFilter đã xử lý token
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:5173"); // Frontend URL
        config.addAllowedMethod("*"); // Cho phép tất cả các phương thức HTTP
        config.addAllowedHeader("*"); // Cho phép tất cả các headers
        config.setAllowCredentials(true); // Cho phép cookie

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}