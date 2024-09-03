package com.ABCEnglish.entity;

import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.User;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "Course_Of_User")
@Getter
@Setter
public class CourseOfUser {

    @Id
    @Column(name = "userId")
    private Integer userId;

    @Id
    @Column(name = "courseId")
    private Integer courseId;

    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "courseId", insertable = false, updatable = false)
    private Course course;
}
