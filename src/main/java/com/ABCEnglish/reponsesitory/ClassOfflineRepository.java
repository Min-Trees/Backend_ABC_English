package com.ABCEnglish.reponsesitory;

import com.ABCEnglish.entity.ClassOffline;
import com.ABCEnglish.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClassOfflineRepository extends JpaRepository<ClassOffline, Integer> {
    @Query("SELECT c FROM ClassOffline c WHERE c.course = :course AND c.status = true")
    Page<ClassOffline> findAllByCourse(Course course, Pageable pageable);
}
