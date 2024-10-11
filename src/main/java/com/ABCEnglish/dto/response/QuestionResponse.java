package com.ABCEnglish.dto.response;

import com.ABCEnglish.entity.Exercises;
import com.ABCEnglish.entity.Question;
import com.ABCEnglish.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)

public class QuestionResponse {
    Integer questionId;
    Exercises exercise;
    User creator;
    String text;
    Boolean status;
    java.math.BigDecimal score;
    Question.QuestionType questionType;
    Question.SkillType skillType;
}
