package com.ABCEnglish.reponsesitory;

import com.ABCEnglish.entity.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {
    public Page<Test> findAll(Pageable pageable);
}
