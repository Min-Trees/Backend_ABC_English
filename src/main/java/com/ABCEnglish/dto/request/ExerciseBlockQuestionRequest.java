package com.ABCEnglish.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseBlockQuestionRequest {
    private Integer exerciseBlockId;
    private Integer questionId;
    private Integer orderIndex;
}
