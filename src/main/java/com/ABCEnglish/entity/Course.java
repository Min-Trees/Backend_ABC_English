package com.ABCEnglish.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Entity
@Table(name = "Course")
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;

    private Integer teacherId;
    private String name;
    private String description;
    private String images;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CourseType type;

    private Boolean status;
    private Double fee;
    private Integer quantitySession;

    @Column(name = "start_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date startDatetime;

    @Column(name = "end_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date endDatetime;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updatedAt;

    public enum CourseType {
        ILETS, TOEIC
    }
}
