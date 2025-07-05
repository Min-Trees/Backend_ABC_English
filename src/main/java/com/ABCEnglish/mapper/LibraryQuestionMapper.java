package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.LibraryQuestionRequest;
import com.ABCEnglish.dto.response.LibraryQuestionResponse;
import com.ABCEnglish.entity.LibraryQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")

public interface LibraryQuestionMapper {
    @Mapping(target = "library.libraryId", source = "libraryId")
    @Mapping(target = "question.questionId", source = "questionId")
    @Mapping(target = "libraryItemId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    LibraryQuestion toEntity(LibraryQuestionRequest request);

    @Mapping(target = "questionId", source = "question.questionId")
    @Mapping(target = "questionText", source = "question.text")
    @Mapping(target = "score", source = "question.score")
    @Mapping(target = "questionType", source = "question.questionType")
    @Mapping(target = "skillType", source = "question.skillType")
    LibraryQuestionResponse toResponse(LibraryQuestion entity);

    @Mapping(target = "library.libraryId", source = "libraryId")
    @Mapping(target = "question.questionId", source = "questionId")
    @Mapping(target = "libraryItemId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    LibraryQuestion updateFromRequest(LibraryQuestionRequest request, @MappingTarget LibraryQuestion entity);

    List<LibraryQuestionResponse> toResponseList(List<LibraryQuestion> items);
}
