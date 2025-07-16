package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.LessonRequest;
import com.ABCEnglish.dto.response.LessonResponse;
import com.ABCEnglish.entity.Lesson;
import com.ABCEnglish.entity.LessonTopic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface LessonMapper {
    @Mapping(target = "topic", expression = "java(mapStringToTopic(request.getTopic()))")
    Lesson toLesson(LessonRequest request);
    @Mapping(target = "topic", expression = "java(lesson.getTopic() != null ? lesson.getTopic().name() : null)")
    @Mapping(source = "level", target = "level")
    @Mapping(source = "lessonId", target = "lessonId")
    @Mapping(source = "course.name", target = "course")
    @Mapping(source = "creator.userId", target = "creator")  // Mapping creator's userId to creator field
    @Mapping(source = "createdAt", target = "createdAt")      // Mapping createdAt field
    @Mapping(source = "updatedAt", target = "updatedAt")
    LessonResponse lessonResponse(Lesson lesson);

    @Mapping(target = "topic", expression = "java(mapStringToTopic(request.getTopic()))")
    void updateLesson(LessonRequest request, @MappingTarget Lesson lesson);
    default LessonTopic mapStringToTopic(String topicName) {
        if (topicName == null) return null;
        try {
            return LessonTopic.valueOf(topicName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid topic name: " + topicName);
        }
    }
}
