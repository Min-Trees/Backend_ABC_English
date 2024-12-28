package com.ABCEnglish.reponsesitory;

import aj.org.objectweb.asm.commons.Remapper;
import com.ABCEnglish.dto.response.SearchResult;
import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;
@Repository
public interface CourseRepository extends JpaRepository<Course, Integer>, JpaSpecificationExecutor<Course> {
    Optional<Course> findByCourseIdAndCreator(Integer courseId, User creator);
    Page<Course> findAllCoureByCreator(User creator, Pageable pageable);
    @Query("SELECT c FROM Course c WHERE c.status = true")
    Page<Course> findAllBy(Pageable pageable);
    Optional<Course> findByCourseId(Integer courseId);




}
