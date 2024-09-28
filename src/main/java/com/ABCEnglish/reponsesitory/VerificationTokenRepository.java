package com.ABCEnglish.reponsesitory;

import com.ABCEnglish.entity.VerifiTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerifiTokenEntity, Long> {
    VerifiTokenEntity findByToken(String token);
}