package com.ABCEnglish.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionDeleteResponse {
    int questionId;
    boolean status;
    String message;
}
