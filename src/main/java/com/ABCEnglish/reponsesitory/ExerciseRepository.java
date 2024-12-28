package com.ABCEnglish.reponsesitory;

import com.ABCEnglish.entity.Exercises;
import com.ABCEnglish.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ExerciseRepository extends JpaRepository<Exercises, Integer>, JpaSpecificationExecutor<Exercises> {
    @Query("SELECT e FROM Exercises e WHERE e.lesson = :lesson AND e.status = true")
    Page<Exercises> findByLesson(Pageable pageable, Lesson lesson);
    List<Exercises> findByLesson(Lesson lesson);
}
