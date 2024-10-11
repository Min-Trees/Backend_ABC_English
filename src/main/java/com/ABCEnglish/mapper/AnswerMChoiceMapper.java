package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.AnswerMChoiceRequest;
import com.ABCEnglish.dto.response.AnswerMChoiceResponse;
import com.ABCEnglish.entity.AnswerMChoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AnswerMChoiceMapper {
    AnswerMChoice toAnswerMchoice(AnswerMChoiceRequest request);
    @Mapping(source = "question.questionId", target = "question")
    @Mapping(source = "user.userId", target = "user")
    AnswerMChoiceResponse answerMChoiceResponse(AnswerMChoice answerMChoice);

    void updateAnswerMChoice(AnswerMChoiceRequest request, @MappingTarget AnswerMChoice answerMChoice);

}
