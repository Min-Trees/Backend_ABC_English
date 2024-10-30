package com.ABCEnglish.dto.request;

import com.ABCEnglish.entity.Question;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnswerEssayRequest {
    Integer question;
    String userEssay;
    Boolean isCorrect;
}
