package com.ABCEnglish.dto.request;

import com.ABCEnglish.entity.Question;
import com.ABCEnglish.entity.QuestionType;
import com.ABCEnglish.entity.SkillType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionRequest {
    String text;
    java.math.BigDecimal score;
    Boolean status;
    QuestionType questionType;
    SkillType skillType;

}
