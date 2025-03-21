package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.response.AttendanceResponse;
import com.ABCEnglish.entity.Attendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {
    AttendanceMapper INSTANCE = Mappers.getMapper(AttendanceMapper.class);

    @Mapping(source = "attendanceId", target = "attendanceId")
    @Mapping(source = "session.classOffline.teacher", target = "teacher")
    @Mapping(source = "student", target = "student")
    @Mapping(source = "session", target = "session")
    @Mapping(source = "attendance", target = "attendance")  // Đổi từ status → attendance đúng với entity
    @Mapping(source = "feedBack", target = "feedBack")      // Đúng với entity
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    AttendanceResponse toResponse(Attendance attendance);
}
