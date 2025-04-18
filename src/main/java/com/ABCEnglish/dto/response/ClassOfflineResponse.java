package com.ABCEnglish.dto.response;

import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.DayOfWeek;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassOfflineResponse {
    Integer classId;
    String course;
    String name;
    String description;
    LocalTime startTime;
    LocalTime endTime;
    Boolean status;
    Date startDate;
    Date endDate;
    List<String> dayOfWeekList;
    Integer quantitySession;
    Date createdAt;
    Date updatedAt;
}
