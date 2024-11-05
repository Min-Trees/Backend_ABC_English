package com.ABCEnglish.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResultExerciseResponse {
    Integer resultExercisesId;
    Integer exerciseId;
    Integer userId;
    Double score;
    Date createdAt;
    Date updatedAt;
}
