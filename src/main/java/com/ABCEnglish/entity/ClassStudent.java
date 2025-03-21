package com.ABCEnglish.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "class_student")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer classStudentId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "class_id",  nullable = false)
    ClassOffline classOffline;
}
