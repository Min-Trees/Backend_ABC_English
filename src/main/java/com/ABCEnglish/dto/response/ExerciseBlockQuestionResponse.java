package com.ABCEnglish.dto.response;

import com.ABCEnglish.entity.Question;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseBlockQuestionResponse {
    private Integer exerciseBlockQuestionId;
    private Integer exerciseBlockId;
    private QuestionWithAnswersResponse question;
    private Integer orderIndex;
}
