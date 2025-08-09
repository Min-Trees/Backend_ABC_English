package com.ABCEnglish.entity;

import jakarta.persistence.*;
        import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "exercise_block_question")
@Getter
@Setter
public class ExerciseBlockQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_block_question_id")
    private Integer exerciseBlockQuestionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_block_id", nullable = false)
    private ExerciseBlock exerciseBlock;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "order_index")
    private Integer orderIndex = 0;
}