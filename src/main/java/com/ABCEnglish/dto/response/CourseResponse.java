package com.ABCEnglish.dto.response;

import com.ABCEnglish.entity.CourseType;
import com.ABCEnglish.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseResponse {
    Integer courseId;
    Integer creator;
    Integer teacher;
    String name;
    String description;
    String image;
    CourseType type;
    Boolean status;
    Double fee;
    Integer quantitySession;
    Date startDatetime;
    Date endDatetime;
    Date createdAt;
    Date updatedAt;
}
