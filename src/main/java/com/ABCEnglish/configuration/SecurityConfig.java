package com.ABCEnglish.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Value("${jwt.signerKey}")
    private String signerKey;
    private final JwtLoggingFilter jwtLoggingFilter;

    public SecurityConfig(JwtLoggingFilter jwtLoggingFilter) {
        this.jwtLoggingFilter = jwtLoggingFilter;
    }
    // Cấu hình phân quyền
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> {
            request.requestMatchers("/api/v1/auth/**").permitAll();
            request.requestMatchers("/api/v1/verify/{token}").permitAll();
            request.requestMatchers(HttpMethod.POST, "/api/v1/users/**").permitAll();
            request.requestMatchers(HttpMethod.GET, "/api/v1/course").permitAll();
            request.requestMatchers(HttpMethod.GET, "/api/v1/document/{courseId}").permitAll();
            request.requestMatchers(HttpMethod.GET, "/api/v1/exercise/{lessonId}").permitAll();
            request.requestMatchers(HttpMethod.GET, "/api/v1/question/{exerciseId}").permitAll();
            request.requestMatchers(HttpMethod.GET, "/api/v1/answer/{questionId}").permitAll();
            request.requestMatchers(HttpMethod.GET, "/api/v1/lesson/{courseId}").permitAll();
            request.anyRequest().authenticated();
        });


        // Thêm JwtLoggingFilter trước UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtLoggingFilter, UsernamePasswordAuthenticationFilter.class);

        // Cấu hình CORS và CSRF
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())));
        return http.build(); // Kết thúc cấu hình và trả về SecurityFilterChain
    }
    @Bean
    JwtDecoder jwtDecoder(){
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
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

    // Cấu hình UserDetailsService (dùng dữ liệu mẫu)
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // Tạo người dùng mẫu để thử nghiệm
            if ("admin".equals(username)) {
                return User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode("admin123")) // Mật khẩu đã mã hóa
                        .roles("admin") // Gán vai trò ADMIN
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
