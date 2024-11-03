package com.ABCEnglish.controller;

import com.ABCEnglish.entity.User;
import com.ABCEnglish.entity.VerifiTokenEntity;
import com.ABCEnglish.entity.VerificationToken;
import com.ABCEnglish.reponsesitory.UserRepository;
import com.ABCEnglish.reponsesitory.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class VerificationController {
    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/v1/verify/{token}")
    public String verifyEmail(@PathVariable("token") String token) {
        VerifiTokenEntity verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "Invalid or expired token!";
        }
        System.out.println(verificationToken.getExpiryDate());
        User user = verificationToken.getUser();
        user.setStatus(true);  // Activate user account
        userRepository.save(user);

        tokenRepository.delete(verificationToken);  // Remove token after successful verification

        return "Email verified successfully!";
    }
}
