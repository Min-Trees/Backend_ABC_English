package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.request.QuestionRequest;
import com.ABCEnglish.dto.response.QuestionDeleteResponse;
import com.ABCEnglish.entity.Exercises;
import com.ABCEnglish.entity.Question;
import com.ABCEnglish.dto.response.QuestionResponse;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exceptioin.AppException;
import com.ABCEnglish.exceptioin.ErrorCode;
import com.ABCEnglish.mapper.QuestionMapper;
import com.ABCEnglish.reponsesitory.ExerciseRepository;
import com.ABCEnglish.reponsesitory.QuestionRepository;
import com.ABCEnglish.reponsesitory.UserRepository;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionService {
    QuestionRepository questionRepository;
    UserRepository userRepository;
    ExerciseRepository exerciseRepository;
    QuestionMapper questionMapper;
    AuthenticationService authenticationService;

    public QuestionResponse createQuestion(Integer exerciseId, QuestionRequest request, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        // kiem tra su ton tai cua user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Exercises exercises = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND));
        Question question = questionMapper.toQuestion(request);
        question.setCreator(user);
        question.setExercise(exercises);
        questionRepository.save(question);
        return questionMapper.questionResponse(question);
    }

    public QuestionResponse updateQuestion(Integer exerciseId, Integer questionId, QuestionRequest request,IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Exercises exercises = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
        questionMapper.updateQuestion(request,question);
        questionRepository.save(question);
        return questionMapper.questionResponse(question);
    }
    public Page<QuestionResponse> getAllQuestion(Pageable pageable, Integer exerciseId){
        // thuc hien phan trang
        Page<Question> questions = questionRepository.findByExercise_ExerciseId(pageable,exerciseId);
        return questions.map(questionMapper::questionResponse);
    }

    public QuestionDeleteResponse deleteQuestion(Integer exerciseId, Integer questionId, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Exercises exercises = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
        questionRepository.delete(question);
        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse();
        questionDeleteResponse.setQuestionId(questionId);
        questionDeleteResponse.setStatus(false);
        questionDeleteResponse.setMessage("delete success");
        return ApiResponse.<QuestionDeleteResponse>builder().result(questionDeleteResponse).build().getResult();
    }
}
