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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "Test")
@Getter
@Setter
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer testId;

    private String title;

    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;

    private Integer level;

    @Column(name = "start_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date startDatetime;

    @Column(name = "end_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date endDatetime;

    @Column(name = "date_done")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date dateDone;

    private Double score;
    private Boolean status;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updatedAt;
}
