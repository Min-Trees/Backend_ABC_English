package com.ABCEnglish.repository;

import com.ABCEnglish.entity.ClassOffline;
import com.ABCEnglish.entity.ClassStudent;
import com.ABCEnglish.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassStudentRepository extends JpaRepository<ClassStudent, Integer> {
    Page<ClassStudent> findByClassOffline(Pageable pageable, ClassOffline classOffline);
    Optional<ClassStudent> findByUserAndClassOffline(User user, ClassOffline classOffline);
    List<ClassStudent> findByClassOffline(ClassOffline classOffline);
}
