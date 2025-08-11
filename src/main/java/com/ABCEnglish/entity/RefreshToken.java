package com.ABCEnglish.entity;

import jakarta.persistence.*;

import java.time.Instant;
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    private User user;

    private Instant expiryDate;
}
