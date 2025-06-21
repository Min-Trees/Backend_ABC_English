package com.ABCEnglish.repository;

import com.ABCEnglish.entity.AnswerMChoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerMChoiceRepository extends JpaRepository<AnswerMChoice, Integer> {
    List<AnswerMChoice> findByQuestion_QuestionId(Integer questionId);

}
