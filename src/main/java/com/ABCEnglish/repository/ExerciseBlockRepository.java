package com.ABCEnglish.repository;

import com.ABCEnglish.entity.ExerciseBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ExerciseBlockRepository extends JpaRepository<ExerciseBlock,Integer> {
    List<ExerciseBlock> findByExercise_ExerciseIdOrderByOrderIndexAsc(Integer exerciseId);
    boolean existsByExerciseBlockIdAndExercise_ExerciseId(Integer blockId, Integer exerciseId);
}
