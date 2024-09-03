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
@Table(name = "Result_test")
@Getter
@Setter
public class ResultTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer resultTestId;

    @ManyToOne
    @JoinColumn(name = "testId", nullable = false)
    private Test test;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    private Double score;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updatedAt;
}
