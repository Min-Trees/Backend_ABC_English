package com.ABCEnglish.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.signerKey}")
    private String signerKey;  // Khóa bí mật để ký JWT (phải >= 32 ký tự)

    @Value("${jwt.valid-duration}")
    private long validDuration;  // Thời gian hợp lệ của token (giây)

    @Value("${jwt.refreshable-duration}")
    private long refreshableDuration;  // Thời gian token có thể được làm mới (giây)

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(signerKey.getBytes());
    }

    // Tạo JWT token
    public String generateToken(String username, Integer userId) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validDuration * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Integer extractUserId(String token) {
        return getClaims(token).get("userId", Integer.class);
    }

    // Trích xuất username từ token
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Kiểm tra token có hợp lệ không
    public boolean validateToken(String token, String username) {
        Claims claims = getClaims(token);
        String tokenUsername = claims.getSubject();
        Date expiration = claims.getExpiration();
        return tokenUsername.equals(username) && !expiration.before(new Date());
    }

    // Kiểm tra token có hết hạn không
    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    // Lấy thời gian còn lại của token (giây)
    public long getRemainingTime(String token) {
        Date expiration = getClaims(token).getExpiration();
        long diff = expiration.getTime() - System.currentTimeMillis();
        return diff > 0 ? diff / 1000 : 0;
    }

    // Trích xuất claims từ token
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
