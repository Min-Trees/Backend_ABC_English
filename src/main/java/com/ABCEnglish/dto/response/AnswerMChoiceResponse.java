package com.ABCEnglish.dto.response;

import com.ABCEnglish.entity.Question;
import com.ABCEnglish.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerMChoiceResponse {
    Integer answerId;
    Question question;
     String content;
     User userId;
     Boolean isCorrect;
     Boolean status;
}
