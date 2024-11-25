package com.ABCEnglish.reponsesitory;

import com.ABCEnglish.entity.Exercises;
import com.ABCEnglish.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository

public interface ExerciseRepository extends JpaRepository<Exercises, Integer>, JpaSpecificationExecutor<Exercises> {
    Page<Exercises> findByLesson(Pageable pageable, Lesson lesson);
}
