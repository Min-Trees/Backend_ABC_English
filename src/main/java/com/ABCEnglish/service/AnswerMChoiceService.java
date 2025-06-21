package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.AnswerMChoiceRequest;
import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.AnswerMChoiceDeleteResponse;
import com.ABCEnglish.dto.response.AnswerMChoiceResponse;
import com.ABCEnglish.entity.AnswerMChoice;
import com.ABCEnglish.entity.Question;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exception.AppException;
import com.ABCEnglish.exception.ErrorCode;
import com.ABCEnglish.mapper.AnswerMChoiceMapper;
import com.ABCEnglish.repository.AnswerMChoiceRepository;
import com.ABCEnglish.repository.QuestionRepository;
import com.ABCEnglish.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnswerMChoiceService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    AnswerMChoiceRepository answerMChoiceRepository;
    @Autowired
    AnswerMChoiceMapper answerMap;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    private TokenService tokenService;

    public AnswerMChoiceResponse createAnswerMChoice(Integer questionId, AnswerMChoiceRequest request, String token) throws ParseException, JOSEException {
        User user = tokenService.getTeacherOrAdmin(token,true);
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
        AnswerMChoice answerMChoice = answerMap.toAnswerMchoice(request);
        answerMChoice.setUser(user);
        answerMChoice.setQuestion(question);
        answerMChoiceRepository.save(answerMChoice);
        return answerMap.answerMChoiceResponse(answerMChoice);
    }

    public AnswerMChoiceResponse updateAnswerMChoice(Integer questionId, Integer answerId, AnswerMChoiceRequest request,String token) throws ParseException, JOSEException {
        User user = tokenService.getTeacherOrAdmin(token,true);
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
        AnswerMChoice answerMChoice = answerMChoiceRepository.findById(answerId).
                orElseThrow(() -> new AppException(ErrorCode.ANSWER_M_CHOICE_NOT_FOUND));
        answerMap.updateAnswerMChoice(request,answerMChoice);

        AnswerMChoice updateAnswerMChoice = answerMChoiceRepository.save(answerMChoice);
        return answerMap.answerMChoiceResponse(updateAnswerMChoice);
    }

    public List<AnswerMChoiceResponse> getAllAnswersMChoice(Integer questionId) {
        return answerMChoiceRepository.findByQuestion_QuestionId(questionId)
                .stream()
                .map(answer -> answerMap.answerMChoiceResponse(answer))
                .collect(Collectors.toList());
    }

    public AnswerMChoiceResponse getAnswerMChoice(Integer questionId, Integer answerId, String token) throws ParseException, JOSEException {
        User user = tokenService.getTeacherOrAdmin(token,true);
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
        AnswerMChoice answerMChoice = answerMChoiceRepository.findById(answerId).
                orElseThrow(() -> new AppException(ErrorCode.ANSWER_M_CHOICE_NOT_FOUND));

        return answerMap.answerMChoiceResponse(answerMChoice);

    }
    public AnswerMChoiceDeleteResponse deleteAnswerMChoiceResponse(Integer questionId, Integer answerId,String token) throws ParseException, JOSEException {
        User user = tokenService.getTeacherOrAdmin(token,true);
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
