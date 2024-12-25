package com.ABCEnglish.reponsesitory;

import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    Page<Lesson> findByCourse(Course course, Pageable pageable);
    List<Lesson> findByCourse(Course course);
}
