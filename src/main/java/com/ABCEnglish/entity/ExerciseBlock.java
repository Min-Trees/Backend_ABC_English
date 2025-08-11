package com.ABCEnglish.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "exercise_block")
@Getter
@Setter
public class ExerciseBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer exerciseBlockId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercises exercise;
    private Integer orderIndex;
    private String title;
    private String description;

}
