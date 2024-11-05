package com.ABCEnglish.reponsesitory;

import com.ABCEnglish.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByExercise_ExerciseId(Integer exerciseId);
    @Query(value = "SELECT COALESCE(SUM(score), 0) FROM Question WHERE exercise_id = :exerciseId", nativeQuery = true)
    Double getTotalScoreByExerciseId(@Param("exerciseId") Integer exerciseId);
}
