package com.ABCEnglish.dto.response;

import com.ABCEnglish.entity.AttendanceStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttendanceStudentResponse {
    Integer attendanceId;
    AttendanceStatus attendanceStatus;
}
