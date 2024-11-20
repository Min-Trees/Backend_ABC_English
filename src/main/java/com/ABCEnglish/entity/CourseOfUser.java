package com.ABCEnglish.entity;

import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Course_Of_User")
@Getter
@Setter
public class CourseOfUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseOfUserId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;
}
