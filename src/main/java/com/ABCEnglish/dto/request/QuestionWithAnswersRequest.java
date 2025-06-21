package com.ABCEnglish.dto.request;

import lombok.Data;

import java.util.List;
@Data
public class QuestionWithAnswersRequest {
    private QuestionRequest question; // giữ nguyên cấu trúc hiện có
    private List<AnswerMChoiceRequest> answers;
}
