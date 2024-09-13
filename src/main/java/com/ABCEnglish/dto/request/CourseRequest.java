package com.ABCEnglish.dto.request;

import com.ABCEnglish.entity.CourseType;
import com.ABCEnglish.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseRequest {
    User teacherId;
    User creater;
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
