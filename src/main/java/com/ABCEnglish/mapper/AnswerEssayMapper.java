package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.AnswerEssayRequest;

import com.ABCEnglish.dto.response.AnswerEssayResponse;
import com.ABCEnglish.entity.AnswerEssay;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AnswerEssayMapper {
    AnswerEssay toAnswerEssay(AnswerEssayRequest request);

    @Mapping(source = "question.questionId", target = "questionId")
    @Mapping(source = "user.userId", target = "userId")
    AnswerEssayResponse answerEssayResponse(AnswerEssay answerEssay);

    void updateANS(AnswerEssayRequest request, @MappingTarget AnswerEssay answerEssay);
}
