package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.request.QuestionRequest;
import com.ABCEnglish.dto.response.QuestionDeleteResponse;
import com.ABCEnglish.entity.Exercises;
import com.ABCEnglish.entity.Question;
import com.ABCEnglish.dto.response.QuestionResponse;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exception.AppException;
import com.ABCEnglish.exception.ErrorCode;
import com.ABCEnglish.mapper.QuestionMapper;
import com.ABCEnglish.repository.ExerciseRepository;
import com.ABCEnglish.repository.QuestionRepository;
import com.ABCEnglish.repository.UserRepository;
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
    private final TokenService tokenService;

    public QuestionResponse createQuestion(Integer exerciseId, QuestionRequest request, String token){
        try {
            User user = tokenService.getTeacherOrAdmin(token, true);
            Exercises exercises = exerciseRepository.findById(exerciseId)
                    .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND));
            Question question = questionMapper.toQuestion(request);
            question.setCreator(user);
            question.setExercise(exercises);
            questionRepository.save(question);
            return questionMapper.questionResponse(question);
        } catch (AppException e) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
    }

    public QuestionResponse updateQuestion(Integer exerciseId, Integer questionId, QuestionRequest request,String token){
        try {
            User user = tokenService.getTeacherOrAdmin(token, true);
            Exercises exercises = exerciseRepository.findById(exerciseId)
                    .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND));
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
            questionMapper.updateQuestion(request, question);
            questionRepository.save(question);
            return questionMapper.questionResponse(question);
        } catch (AppException e) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
    }
    public Page<QuestionResponse> getAllQuestion(Pageable pageable, Integer exerciseId) {
        // thuc hien phan trang
        Page<Question> questions = questionRepository.findByExercise_ExerciseId(pageable,exerciseId);
        return questions.map(questionMapper::questionResponse);
    }

    public QuestionDeleteResponse deleteQuestion(Integer exerciseId, Integer questionId, String token) {
        try {
            User user = tokenService.getTeacherOrAdmin(token, true);
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
        } catch (AppException e) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
    }
}
