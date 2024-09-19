package com.ABCEnglish.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonRequest {
    Integer lessonIndex;
    String name;
    String content;
    Boolean status;
}
