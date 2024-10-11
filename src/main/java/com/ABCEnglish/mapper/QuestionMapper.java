package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.QuestionRequest;
import com.ABCEnglish.entity.Question;
import com.ABCEnglish.dto.response.QuestionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    Question toQuestion(QuestionRequest request);
    @Mapping(source = "creator.userId", target = "creator")
    @Mapping(source = "exercise.exerciseId", target = "exercise")
    QuestionResponse questionResponse(Question question);
    void updateQuestion(QuestionRequest request, @MappingTarget Question question);
}
