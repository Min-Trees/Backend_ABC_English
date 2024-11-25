package com.ABCEnglish.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassOfflineRequest {
    String name;
    String description;
    LocalTime startTime;
    LocalTime endTime;
    Date startDate;
    Date endDate;
    List<String> dayOfWeekList;
}
