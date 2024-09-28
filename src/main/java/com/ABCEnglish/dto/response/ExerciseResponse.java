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
public class ExerciseResponse {
    Integer exerciseId;
    Course course;
    User user;
    String title;
    String content;
    Date createdAt;
    Date updatedAt;
    Boolean status;
}
