package com.ABCEnglish.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name = "Attendance")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer attendanceId;
    @ManyToOne
    User teacher;
    @ManyToOne
    User student;
    @ManyToOne
    @JoinColumn(name = "session_id")
    ClassOfflineSession session;
    @Enumerated(EnumType.STRING)
    AttendanceStatus attendance;
    String feedBack;
    Date createdAt;
    Date updatedAt;
}

