package com.ABCEnglish.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerifyToken {
    Integer tokenId;
    String token;
    @OneToOne
    @JoinColumn(name = "userId")
    User userId;
    LocalDateTime expiryDate;

    public VerifyToken(User userId, String token){
        this.userId = userId;
        this.token = token;
        this.expiryDate = LocalDateTime.now().plusMinutes(30);
    }
}
