package com.ABCEnglish.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ExerciseBlockResponse {
    private Integer exerciseBlockId;
    private String title;
    private String description;
    private Integer orderIndex;
    private Integer exerciseId;
}
