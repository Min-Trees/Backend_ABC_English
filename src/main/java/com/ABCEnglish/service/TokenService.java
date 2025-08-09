package com.ABCEnglish.service;

import com.ABCEnglish.configuration.JwtUtil;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.entity.VerifiTokenEntity;
import com.ABCEnglish.exception.AppException;
import com.ABCEnglish.exception.ErrorCode;
import com.ABCEnglish.repository.UserRepository;
import com.ABCEnglish.repository.VerificationTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    /**
     * Kiểm tra token hợp lệ (không hết hạn và chưa bị thu hồi)
     */
    public void validateToken(String token) {
        VerifiTokenEntity storedToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

        if (storedToken.isRevoked() || storedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            logger.warn("❌ Token is invalid or expired: {}", token);
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        logger.info("✅ Token is valid: {}", token);
    }

    /**
     * Lấy user nếu là TEACHER hoặc ADMIN
     */
    public User getTeacherOrAdmin(String token, boolean requireMaster) {
        Integer userId = jwtUtil.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String role = user.getRole().getName();
        if (requireMaster && !("ADMIN".equalsIgnoreCase(role) || "TEACHER".equalsIgnoreCase(role))) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        return user;
    }

    /**
     * Lấy user nếu là ADMIN
     */
    public User getAdmin(String token, boolean requireAdmin) {
        Integer userId = jwtUtil.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (requireAdmin && !"ADMIN".equalsIgnoreCase(user.getRole().getName())) {
            logger.warn("❌ User {} does not have admin rights", userId);
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        logger.info("✅ User retrieved: userId={}, isAdmin={}", userId, true);
        return user;
    }

    /**
     * Lấy user từ token nếu tồn tại trong DB
     */
    public User getUserFromToken(String token) {
        try {
            Integer userId = jwtUtil.extractUserId(token);
            return userRepository.findById(userId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    /**
     * Lưu token mới vào DB (cho phép nhiều token tồn tại)
     */
    public void saveToken(User user, String token, long ttlInSeconds) {
        VerifiTokenEntity newToken = new VerifiTokenEntity(user, token);
        newToken.setExpiryDate(LocalDateTime.now().plusSeconds(ttlInSeconds));
        newToken.setRevoked(false);
        verificationTokenRepository.save(newToken);
        logger.info("✅ Saved new token for userId={}", user.getUserId());
    }

    /**
     * Trả lại token nếu tồn tại
     */
    public String getToken(String token) {
        return verificationTokenRepository.findByToken(token)
                .map(VerifiTokenEntity::getToken)
                .orElse(null);
    }

    /**
     * Kiểm tra token hợp lệ với username
     */
    public boolean isTokenValid(String username, String token) {
        User user = userRepository.findByPhone(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return verificationTokenRepository.findByUser(user).stream()
                .anyMatch(t -> t.getToken().equals(token)
                        && !t.isRevoked()
                        && t.getExpiryDate().isAfter(LocalDateTime.now()));
    }

    /**
     * Xóa các token hết hạn khỏi DB
     */
    public void cleanExpiredTokens() {
        verificationTokenRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
        logger.info("🧹 Dọn dẹp các token hết hạn thành công!");
    }

    /**
     * Thu hồi (vô hiệu hóa) token cụ thể
     */
    public boolean revokeToken(String token) {
        return verificationTokenRepository.findByToken(token).map(t -> {
            t.setRevoked(true);
            verificationTokenRepository.save(t);
            logger.info("❎ Token revoked: {}", token);
            return true;
        }).orElse(false);
    }
}
