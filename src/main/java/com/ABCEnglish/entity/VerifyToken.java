package com.ABCEnglish.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "verify_token")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerifyToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    Integer tokenId;

    @Column(nullable = false, unique = true, length = 512)
    String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "expiry_date", nullable = false)
    LocalDateTime expiryDate;

    public VerifyToken(User user, String token) {
        this.user = user;
        this.token = token;
        this.expiryDate = LocalDateTime.now().plusMinutes(30); // hoặc bạn tùy chỉnh
    }
}
