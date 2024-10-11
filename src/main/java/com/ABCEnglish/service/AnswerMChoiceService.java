package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.AnswerMChoiceRequest;
import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.AnswerMChoiceDeleteResponse;
import com.ABCEnglish.dto.response.AnswerMChoiceResponse;
import com.ABCEnglish.dto.response.ExerciseDeleteResponse;
import com.ABCEnglish.dto.response.ExerciseResponse;
import com.ABCEnglish.entity.AnswerMChoice;
import com.ABCEnglish.entity.Exercises;
import com.ABCEnglish.entity.Question;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exceptioin.AppException;
import com.ABCEnglish.exceptioin.ErrorCode;
import com.ABCEnglish.mapper.AnswerMChoiceMapper;
import com.ABCEnglish.reponsesitory.AnswerMChoiceRepository;
import com.ABCEnglish.reponsesitory.QuestionRepository;
import com.ABCEnglish.reponsesitory.UserRepository;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnswerMChoiceService {
    UserRepository userRepository;
    QuestionRepository questionRepository;
    AnswerMChoiceRepository answerMChoiceRepository;
    AnswerMChoiceMapper answerMap;
    AuthenticationService authenticationService;

    public AnswerMChoiceResponse createAnswerMChoice(Integer questionId, AnswerMChoiceRequest request, IntrospectRequest token) throws ParseException, JOSEException {

        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
        AnswerMChoice answerMChoice = answerMap.toAnswerMchoice(request);
        answerMChoice.setUser(user);
        answerMChoice.setQuestion(question);
        answerMChoiceRepository.save(answerMChoice);
        return answerMap.answerMChoiceResponse(answerMChoice);
    }

    public AnswerMChoiceResponse updateAnswerMChoice(Integer questionId, Integer answerId, AnswerMChoiceRequest request,IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
        AnswerMChoice answerMChoice = answerMChoiceRepository.findById(answerId).
                orElseThrow(() -> new AppException(ErrorCode.ANSWER_M_CHOICE_NOT_FOUND));
        answerMap.updateAnswerMChoice(request,answerMChoice);

        AnswerMChoice updateAnswerMChoice = answerMChoiceRepository.save(answerMChoice);
        return answerMap.answerMChoiceResponse(updateAnswerMChoice);
    }

    public Page<AnswerMChoiceResponse> getAllAnswersMChoice(Pageable pageable){
        // thuc hien phan trang
        Page<AnswerMChoice> answerMChoices = answerMChoiceRepository.findAll(pageable);
        return answerMChoices.map(answerMap::answerMChoiceResponse);
    }

    public AnswerMChoiceResponse getAnswerMChoice(Integer questionId, Integer answerId, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
        AnswerMChoice answerMChoice = answerMChoiceRepository.findById(answerId).
                orElseThrow(() -> new AppException(ErrorCode.ANSWER_M_CHOICE_NOT_FOUND));

        return answerMap.answerMChoiceResponse(answerMChoice);

    }
    public AnswerMChoiceDeleteResponse deleteAnswerMChoiceResponse(Integer questionId, Integer answerId,IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
        AnswerMChoice answerMChoice = answerMChoiceRepository.findById(answerId).
                orElseThrow(() -> new AppException(ErrorCode.ANSWER_M_CHOICE_NOT_FOUND));

        answerMChoiceRepository.delete(answerMChoice);
        AnswerMChoiceDeleteResponse result = new AnswerMChoiceDeleteResponse();
        result.setAnswerId(answerId);
        result.setStatus(false);
        result.setMessage("Delete success");
        return ApiResponse.<AnswerMChoiceDeleteResponse>builder().result(result).build().getResult();
    }


}
