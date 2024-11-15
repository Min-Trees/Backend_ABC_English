package com.ABCEnglish.reponsesitory;

import com.ABCEnglish.entity.AnswerMChoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerMChoiceRepository extends JpaRepository<AnswerMChoice, Integer> {
    Page<AnswerMChoice> findByQuestion_QuestionId(Pageable pageable, Integer questionId);
}
