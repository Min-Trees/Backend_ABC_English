package com.ABCEnglish.repository;

import com.ABCEnglish.entity.User;
import com.ABCEnglish.entity.VerifiTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
@Repository
public interface VerificationTokenRepository extends JpaRepository<VerifiTokenEntity, Long> {
    Optional<VerifiTokenEntity> findByToken(String token);
    void deleteAllByExpiryDateBefore(LocalDateTime now);

    VerifiTokenEntity findByUser(User user);
}