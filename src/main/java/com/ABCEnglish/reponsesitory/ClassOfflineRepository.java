package com.ABCEnglish.reponsesitory;

import com.ABCEnglish.entity.ClassOffline;
import com.ABCEnglish.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassOfflineRepository extends JpaRepository<ClassOffline, Integer> {
    Page<ClassOffline> findAllByCourse(Course course, Pageable pageable);
}
