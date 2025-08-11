package com.ABCEnglish.repository;

import com.ABCEnglish.entity.ExerciseBlockQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseBlockQuestionRepository extends JpaRepository<ExerciseBlockQuestion, Integer> {
    List<ExerciseBlockQuestion> findByExerciseBlock_ExerciseBlockIdOrderByOrderIndexAsc(Integer blockId);
    boolean existsByExerciseBlockQuestionIdAndExerciseBlock_ExerciseBlockId(Integer blockQuestionId, Integer blockId);



}
