package com.ABCEnglish.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "CourseOffline")
@Getter
@Setter
public class CourseOffline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;

    @ManyToOne
    @JoinColumn(name = "teacherId")
    private User teacher;
    @Column(columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String name;

    @Column(columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String description;
    private String image;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Course.CourseType type;

    private Boolean status;
    private Double fee;
    private Integer quantitySession;
    @ElementCollection(targetClass = DayOfWeek.class)
    @CollectionTable(name = "course_days", joinColumns = @JoinColumn(name = "course_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private List<DayOfWeek> daysOfWeek;
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
