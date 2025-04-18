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

@Service
public class TokenService {
    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    public void validateToken(String token) {
        VerifiTokenEntity storedToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.UNEXPECTED_ERROR));
        if (storedToken.isRevoked() || storedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            logger.warn("Token is invalid or expired: {}", token);
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        logger.info("Token is valid: {}", token);
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
    public void saveToken(User user, String token, long ttlInSeconds) {
        VerifiTokenEntity existingToken = verificationTokenRepository.findByUser(user);
        if (existingToken != null) {
            existingToken.setRevoked(true); // Vô hiệu hóa token cũ
            verificationTokenRepository.save(existingToken);
        }

        // Tạo mới token và lưu vào database
        VerifiTokenEntity verifiTokenEntity = new VerifiTokenEntity(user, token);
        verifiTokenEntity.setExpiryDate(LocalDateTime.now().plusSeconds(ttlInSeconds));
        verificationTokenRepository.save(verifiTokenEntity);
    }

    // Lấy token từ Redis
    public String getToken(String token) {
        return verificationTokenRepository.findByToken(token)
                .map(VerifiTokenEntity::getToken)
                .orElse(null);
    }

    // Kiểm tra token có hợp lệ không
    public boolean isTokenValid(String username,String token) {
        User user = userRepository.findByPhone(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        VerifiTokenEntity verifiTokenEntity = verificationTokenRepository.findByUser(user);

        if (verifiTokenEntity == null || verifiTokenEntity.isRevoked()) {
            return false; // Token không tồn tại hoặc đã bị thu hồi
        }

        return verifiTokenEntity.getToken().equals(token) &&
                verifiTokenEntity.getExpiryDate().isAfter(LocalDateTime.now());
    }

    // Xóa token (dùng khi logout)
    public void cleanExpiredTokens() {
        verificationTokenRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
        logger.info("✅ Dọn dẹp các token hết hạn thành công!");
    }

    // Thu hồi token (đăng xuất)
    public boolean revokeToken(String token) {
        verificationTokenRepository.findByToken(token).ifPresent(t -> {
            t.setRevoked(true);
            verificationTokenRepository.save(t);
        });
        return false;
    }


}
