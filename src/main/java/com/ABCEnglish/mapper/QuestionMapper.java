package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.QuestionRequest;
import com.ABCEnglish.entity.Question;
import com.ABCEnglish.dto.response.QuestionResponse;
import com.ABCEnglish.entity.QuestionTopic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    @Mapping(target = "topic", expression = "java(mapStringToTopic(request.getTopic()))")
    Question toQuestion(QuestionRequest request);
    @Mapping(source = "creator.userId", target = "creatorId")
    @Mapping(source = "exercise.exerciseId", target = "exerciseId")
    QuestionResponse questionResponse(Question question);
    @Mapping(target = "topic", expression = "java(mapStringToTopic(request.getTopic()))")
    void updateQuestion(QuestionRequest request, @MappingTarget Question question);
    default QuestionTopic mapStringToTopic(String topicName) {
        if (topicName == null) return null;
        try {
            return QuestionTopic.valueOf(topicName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid topic name: " + topicName);
        }
    }
}
