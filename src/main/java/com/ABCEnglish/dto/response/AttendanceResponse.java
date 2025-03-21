package com.ABCEnglish.dto.response;

import com.ABCEnglish.entity.AttendanceStatus;
import com.ABCEnglish.entity.ClassOfflineSession;
import com.ABCEnglish.entity.Lesson;
import com.ABCEnglish.entity.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttendanceResponse {
    Integer attendanceId;
    User teacher;
    User student;
    ClassOfflineSession session;
    AttendanceStatus attendance;
    String feedBack;
    Date createdAt;
    Date updatedAt;
}
