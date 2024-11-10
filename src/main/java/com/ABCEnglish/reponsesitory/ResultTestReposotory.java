package com.ABCEnglish.reponsesitory;

import com.ABCEnglish.entity.ResultTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultTestReposotory extends JpaRepository<ResultTest,Integer> {
}
