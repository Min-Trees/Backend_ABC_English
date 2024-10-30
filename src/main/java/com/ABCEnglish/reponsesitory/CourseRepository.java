package com.ABCEnglish.reponsesitory;

import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    Optional<Course> findByCourseIdAndCreator(Integer courseId, User creator);

    Page<Course> findAllBy(Pageable pageable);
    Optional<Course> findByCourseId(Integer courseId);
}
