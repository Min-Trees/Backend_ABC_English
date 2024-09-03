package com.ABCEnglish.entity;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "Grammar_Check")
@Getter
@Setter
public class GrammarCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer grammarId;

    @ManyToOne
    @JoinColumn(name = "questionId")
    private Question question;

    private String userText;
    private String correctText;
    private String errorJson;
    private java.math.BigDecimal score;
}
