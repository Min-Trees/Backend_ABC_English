package com.ABCEnglish.reponsesitory;

import com.ABCEnglish.entity.Social;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialRepository extends JpaRepository<Social, Integer> {
    Page<Social> findAllBy(Pageable pageable);
}
