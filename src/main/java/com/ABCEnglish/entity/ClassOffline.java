package com.ABCEnglish.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;
import java.time.DayOfWeek; // Import từ java.time
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "class_offline")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassOffline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer classId;

    @ManyToOne
    @JoinColumn(name = "courseId")
    Course course;

    @Column(columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String name;

    @Column(columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String description;

    LocalTime startTime;

    LocalTime endTime;

    Boolean status = true;

    Date startDate;

    @ManyToOne
    @JoinColumn(name = "teacherId")
    User teacher;

    Date endDate;

    @ElementCollection
    @CollectionTable(name = "class_offline_days", joinColumns = @JoinColumn(name = "class_id"))
    @Enumerated(EnumType.STRING)
    List<DayOfWeek> dayOfWeekList = new ArrayList<>(); // Sử dụng java.time.DayOfWeek

    @OneToMany(mappedBy = "classOffline", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ClassOfflineSession> sessions = new ArrayList<>();

    Integer quantitySession;

    Date createdAt;

    Date updatedAt;
}