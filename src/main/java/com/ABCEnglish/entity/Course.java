package com.ABCEnglish.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "course")  // PostgreSQL nhạy cảm với chữ hoa, nên dùng chữ thường
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")  // Tên cột rõ ràng
    private Integer courseId;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "userId")  // "id" là PK của User
    private User creator;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "userId")
    private User teacher;

    @Column(nullable = false, columnDefinition = "TEXT")  // TEXT thay vì NVARCHAR(MAX)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private CourseType type;

    private Boolean status;
    private Double fee;

    @Column(name = "quantity_session")
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
