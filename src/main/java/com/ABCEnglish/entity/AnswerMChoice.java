package com.ABCEnglish.entity;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "Answer_M_Choice")
@Getter
@Setter
public class AnswerMChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer answerId;

    @ManyToOne
    @JoinColumn(name = "questionId")
    private Question question;
    @Column(columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String content;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    private Boolean isCorrect;
    private Boolean status;
}
