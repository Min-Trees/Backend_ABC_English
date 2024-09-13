package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.CourseRequest;
import com.ABCEnglish.dto.response.CourseResponse;
import com.ABCEnglish.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    Course toCourse(CourseRequest request);
    CourseResponse courseResponse(Course response);

    void updatedCourse(CourseRequest request, @MappingTarget Course course);
}
