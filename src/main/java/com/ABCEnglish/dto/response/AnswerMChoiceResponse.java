package com.ABCEnglish.dto.response;

import com.ABCEnglish.entity.Question;
import com.ABCEnglish.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerMChoiceResponse {
    Integer answerId;
    Integer questionId;
    String content;
    Integer userId;
    Boolean isCorrect;
    Boolean status;
}
