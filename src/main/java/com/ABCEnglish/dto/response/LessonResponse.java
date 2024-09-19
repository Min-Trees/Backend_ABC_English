package com.ABCEnglish.dto.response;

import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonResponse {
    Integer lessonId;
    String course;
    Integer creator;
    Integer lessonIndex;
    String name;
    String content;
    Boolean status;
    Date createdAt;
    Date updatedAt;
}
