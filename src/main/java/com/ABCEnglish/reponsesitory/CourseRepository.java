package com.ABCEnglish.reponsesitory;

import com.ABCEnglish.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    Optional<Course> findUserCreate(Integer integer, Integer creater);

    Page<Course> findByAll(Pageable pageable);
}
