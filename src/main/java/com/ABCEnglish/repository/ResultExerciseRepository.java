package com.ABCEnglish.repository;

import com.ABCEnglish.entity.ResultExercises;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultExerciseRepository extends JpaRepository<ResultExercises, Integer> {
    List<ResultExercises> findByExercise_ExerciseIdAndUser_UserId(Integer exerciseId, Integer userId);
}
