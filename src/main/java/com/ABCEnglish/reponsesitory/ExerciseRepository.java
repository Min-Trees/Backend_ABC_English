package com.ABCEnglish.reponsesitory;

import com.ABCEnglish.entity.Exercises;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ExerciseRepository extends JpaRepository<Exercises, Integer> {

}
