package com.ABCEnglish.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;
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

    Date startDate;

    Date endDate;

    @ElementCollection
    @CollectionTable(name = "class_offline_days", joinColumns = @JoinColumn(name = "class_id"))
    @Enumerated(EnumType.STRING)
    List<DayOfWeek> dayOfWeekList;

    Integer quantitySession;
    Date createdAt;
    Date updatedAt;
}