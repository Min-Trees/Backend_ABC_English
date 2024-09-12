package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.CourseRegister;
import com.ABCEnglish.dto.response.CourseResponse;
import com.ABCEnglish.entity.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    Course toCourse(CourseRegister request);
    CourseResponse courseResponse(Course response);
}
