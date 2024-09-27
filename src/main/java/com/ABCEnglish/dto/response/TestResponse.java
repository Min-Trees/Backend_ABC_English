package com.ABCEnglish.dto.response;

import com.ABCEnglish.entity.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestResponse {
    private Integer testId;
    private String title;
    private Course course;
    private Integer level;
    private java.util.Date startDatetime;
    private java.util.Date endDatetime;
    private java.util.Date dateDone;
    private Double score;
    private Boolean status;
    private java.util.Date createdAt;
    private java.util.Date updatedAt;
}
