package com.ABCEnglish.entity;

import com.ABCEnglish.entity.Course;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "Lesson")
@Getter
@Setter
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lessonId;

    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "creatorId", nullable = false)
    private User creator;

    @Column(name = "lesson_index")
    private Integer lessonIndex;

    private String name;
    private String content;
    private Boolean status;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updatedAt;

}
