package com.ABCEnglish.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Course")
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;
    @ManyToOne
    @JoinColumn(name = "creatorId")
    private User creator;  // Thay vì Integer creatorId

    @ManyToOne
    @JoinColumn(name = "teacherId")
    private User teacher;
    private String name;
    private String description;
    private String image;

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
        IELTS, TOEIC
    }
}
