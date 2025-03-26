package com.ABCEnglish.service;
import com.ABCEnglish.configuration.JwtUtil;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exception.AppException;
import com.ABCEnglish.exception.ErrorCode;
import com.ABCEnglish.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    public void validateToken(String username, String token) {
        try {
            // Kiểm tra token hợp lệ
            if (!isTokenValid(username, token)) {
                logger.warn("Token is invalid for username: {}", username);
                throw new AppException(ErrorCode.UNEXPECTED_ERROR);
            }
            logger.info("Token is valid for username: {}", username);

            // Kiểm tra token bị blacklist
            if (isTokenBlacklisted(token)) {
                logger.warn("Token is blacklisted: {}", token);
                throw new AppException(ErrorCode.TOKEN_BLACKLISTED);
            }
            logger.info("Token is not blacklisted");

        } catch (Exception e) {
            logger.error("Error validating token: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.REDIS_CONNECTION_FAILED);  // Lỗi Redis hoặc lỗi khác
        }
    }

    public User getTeacherOrAdmin(String token, boolean requireMaster) {
        Integer userId = jwtUtil.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        String role = user.getRole().getName();
        if (requireMaster && !("ADMIN".equalsIgnoreCase(role) || "TEACHER".equalsIgnoreCase(role))){
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        return user;
    }

    public User getAdmin(String token, boolean requireAdmin) {
        Integer userId = jwtUtil.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("User not found with userId: {}", userId);
                    return new AppException(ErrorCode.INVALID_TOKEN);
                });
        if (requireAdmin && !"ADMIN".equalsIgnoreCase(user.getRole().getName())) {
            logger.warn("User {} does not have admin rights", userId);
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        logger.info("User retrieved: userId={}, isAdmin={}", userId, !requireAdmin || "ADMIN".equalsIgnoreCase(user.getRole().getName()));
        return user;
    }

    public User getUserFromToken(String token) {
        String storedToken = getToken(token);
        if (storedToken == null) {
            return null;
        }
        Integer userId = jwtUtil.extractUserId(storedToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
        return user;
    }

    // Lưu token vào Redis với thời gian sống (TTL)
    public void saveToken(String username, String token, long ttlInSeconds) {
        redisTemplate.opsForValue().set("token:" + username, token, ttlInSeconds, TimeUnit.SECONDS);
    }

    // Lấy token từ Redis
    public String getToken(String username) {
        return (String) redisTemplate.opsForValue().get("token:" + username);
    }

    // Kiểm tra token có hợp lệ không
    public boolean isTokenValid(String username, String token) {
        String storedToken = getToken(username);
        logger.info("Checking token for username: {}, storedToken: {}, providedToken: {}", username, storedToken, token);
        return storedToken != null && storedToken.equals(token);
    }

    // Xóa token (dùng khi logout)
    public void deleteToken(String username) {
        redisTemplate.delete("token:" + username);
    }

    // Blacklist token (nếu cần vô hiệu hóa token trước khi hết hạn)
    public void blacklistToken(String token, long ttlInSeconds) {
        redisTemplate.opsForValue().set("blacklist:" + token, "true", ttlInSeconds, TimeUnit.SECONDS);
    }

    // Kiểm tra token có trong blacklist không
    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token));
    }
}
