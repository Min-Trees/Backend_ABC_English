package com.ABCEnglish.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseBlockRequest {
    private Integer exerciseId;
    private Integer blockId;
    private String title;
    private String description;
    private Integer orderIndex;
}
