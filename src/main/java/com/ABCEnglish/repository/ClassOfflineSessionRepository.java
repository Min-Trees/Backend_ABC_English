package com.ABCEnglish.repository;

import com.ABCEnglish.entity.ClassOffline;
import com.ABCEnglish.entity.ClassOfflineSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassOfflineSessionRepository extends JpaRepository<ClassOfflineSession, Integer> {
    List<ClassOfflineSession> findByClassOffline(ClassOffline classOffline);
}
