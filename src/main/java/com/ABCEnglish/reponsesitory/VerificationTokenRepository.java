package com.ABCEnglish.reponsesitory;

import com.ABCEnglish.entity.VerifiTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerifiTokenEntity, Long> {
    Optional<VerifiTokenEntity> findByToken(String token);
}