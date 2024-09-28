package com.ABCEnglish.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
public class VerifiTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    private LocalDateTime expiryDate;
    public VerifiTokenEntity(User user, String token) {
        this.user = user;
        this.token = token;
        this.expiryDate = LocalDateTime.now().plusHours(24); // 24-hour expiration
    }

    public VerifiTokenEntity() {

    }
}
