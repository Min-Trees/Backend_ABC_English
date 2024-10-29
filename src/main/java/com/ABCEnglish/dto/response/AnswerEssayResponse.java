package com.ABCEnglish.dto.response;

import com.ABCEnglish.entity.Question;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnswerEssayResponse {
    Integer answerId;
    Question question;
    String userEssay;
    Boolean isCorrect;
}
