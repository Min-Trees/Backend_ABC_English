package com.ABCEnglish.entity;

import com.ABCEnglish.entity.Exercises;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "Question")
@Getter
@Setter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questionId;

    @ManyToOne
    @JoinColumn(name = "exerciseId")
    private Exercises exercise;

    private String text;
    private java.math.BigDecimal score;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Enumerated(EnumType.STRING)
    private SkillType skillType;

    public enum QuestionType {
        MULTIPLE_CHOICE, ESSAY
    }

    public enum SkillType {
        LISTENING, SPEAKING, WRITING, READING
    }
}
