package com.ABCEnglish.dto.request;

import com.ABCEnglish.entity.AttendanceStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttendanceStudentRequest {
    Integer attendanceId;
    AttendanceStatus attendanceStatus;
}
