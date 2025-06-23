package com.ABCEnglish.entity;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "answer_essay")
@Getter
@Setter
public class AnswerEssay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer answerId;

    @ManyToOne
    @JoinColumn(name = "questionId")
    private Question question;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    private Boolean isCorrect;
    private Boolean status;
}
