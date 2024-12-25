package com.ABCEnglish.configuration;

import com.ABCEnglish.service.UserLogService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;

@Component
public class JwtLoggingFilter extends OncePerRequestFilter {
    @Autowired
    UserLogService userLogService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Lấy token từ header Authorization
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Bỏ chữ "Bearer "
        }

        // Lấy IP address
        String ipAddress = request.getRemoteAddr();

        // Lấy action (method và URI)
        String method = request.getMethod();
        String uri = request.getRequestURI();

        // Log thông tin
        System.out.println("User IP: " + ipAddress + ", Action: " + method + " " + uri +
                (token != null ? ", Token: " + token : ", Token: Not Provided"));
        try {
            userLogService.saveLog(token != null ? token : "Not Provided", ipAddress, uri, method);
        } catch (ParseException | JOSEException e) {
            // Log lỗi
            System.err.println("Error saving user log: " + e.getMessage());
            e.printStackTrace();
        }


        // Tiếp tục xử lý request
        filterChain.doFilter(request, response);
    }
}
