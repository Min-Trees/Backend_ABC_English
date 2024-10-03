package com.ABCEnglish.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestRequest {
    String title;
    String courseId;
    Integer level;
    Double score;
    Boolean status;
    Date startDatetime;
    Date endDatetime;
}
