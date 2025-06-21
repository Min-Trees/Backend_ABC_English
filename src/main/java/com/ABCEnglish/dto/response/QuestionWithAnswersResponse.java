package com.ABCEnglish.dto.response;

import lombok.Data;
import lombok.Setter;

import java.util.List;
@Data
public class QuestionWithAnswersResponse {
    private QuestionResponse question;
    private List<AnswerMChoiceResponse> answers;
}
