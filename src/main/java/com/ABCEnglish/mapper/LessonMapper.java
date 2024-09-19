package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.LessonRequest;
import com.ABCEnglish.dto.response.LessonResponse;
import com.ABCEnglish.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface LessonMapper {

    Lesson toLesson(LessonRequest request);
    @Mapping(source = "lessonId", target = "lessonId")
    @Mapping(source = "course.name", target = "course")
    @Mapping(source = "creator.userId", target = "creator")  // Mapping creator's userId to creator field
    @Mapping(source = "createdAt", target = "createdAt")      // Mapping createdAt field
    @Mapping(source = "updatedAt", target = "updatedAt")
    LessonResponse lessonResponse(Lesson lesson);
    void updateLesson(LessonRequest request, @MappingTarget Lesson lesson);
}
