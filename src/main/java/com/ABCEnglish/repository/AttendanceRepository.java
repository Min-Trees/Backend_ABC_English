package com.ABCEnglish.repository;

import com.ABCEnglish.entity.Attendance;
import jakarta.mail.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    List<Attendance> findBySession(Session session);
}
