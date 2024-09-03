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
@Table(name = "Result_exercises")
@Getter
@Setter
public class ResultExercises {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer resultExercisesId;

    @ManyToOne
    @JoinColumn(name = "exerciseId", nullable = false)
    private Exercises exercise;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    private String answer;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updatedAt;
}
