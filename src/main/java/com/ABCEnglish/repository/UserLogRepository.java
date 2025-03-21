package com.ABCEnglish.repository;

import com.ABCEnglish.entity.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLogRepository extends JpaRepository<UserLog, Integer> {
}
