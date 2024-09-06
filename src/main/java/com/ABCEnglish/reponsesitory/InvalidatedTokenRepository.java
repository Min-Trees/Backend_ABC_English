package com.ABCEnglish.reponsesitory;

import com.ABCEnglish.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {}
