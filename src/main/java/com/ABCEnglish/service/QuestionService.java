package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.*;
import com.ABCEnglish.dto.response.AnswerMChoiceResponse;
import com.ABCEnglish.dto.response.QuestionDeleteResponse;
import com.ABCEnglish.dto.response.QuestionWithAnswersResponse;
import com.ABCEnglish.entity.*;
import com.ABCEnglish.dto.response.QuestionResponse;
import com.ABCEnglish.exception.AppException;
import com.ABCEnglish.exception.ErrorCode;
import com.ABCEnglish.mapper.AnswerMChoiceMapper;
import com.ABCEnglish.mapper.QuestionMapper;
import com.ABCEnglish.repository.AnswerMChoiceRepository;
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
import java.util.ArrayList;
import java.util.List;

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
    AnswerMChoiceMapper answerMChoiceMapper;
    AnswerMChoiceRepository answerMChoiceRepository;
    private final AnswerMChoiceService answerMChoiceService;

    public QuestionWithAnswersResponse getQuestionWithAnswer(Integer questionId, String token) {
        try {
            // Kiểm tra quyền
            User user = tokenService.getTeacherOrAdmin(token, true);

            // Lấy câu hỏi
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));

            // Lấy danh sách câu trả lời
            List<AnswerMChoiceResponse> answerMChoices = answerMChoiceService.getAllAnswersMChoice(questionId);

            // Trả response
            QuestionWithAnswersResponse response = new QuestionWithAnswersResponse();
            response.setQuestion(questionMapper.questionResponse(question));
            response.setAnswers(answerMChoices);
            return response;

        } catch (AppException e) {
            // Ghi log lỗi nghiệp vụ rõ ràng
            System.err.println("AppException: " + e.getErrorCode());
            throw e; // Ném lại để controller xử lý
        } catch (Exception e) {
            // Lỗi hệ thống không xác định
            System.err.println("Unexpected error: " + e.getMessage());
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }



    public QuestionWithAnswersResponse createQuestionWithAnswer(Integer exerciseId, QuestionWithAnswersRequest questionRequest, String token) {
        try{
            User user = tokenService.getTeacherOrAdmin(token, true);
            Exercises exercises = exerciseRepository.findById(exerciseId)
                    .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND));
            Question question = questionMapper.toQuestion(questionRequest.getQuestion());
            question.setCreator(user);
            question.setExercise(exercises);
            questionRepository.save(question);
            List<AnswerMChoiceResponse> answerResponses = new ArrayList<>();
            if (questionRequest.getAnswers() != null) {
                for (AnswerMChoiceRequest answerRequest : questionRequest.getAnswers()) {
                    AnswerMChoice answer = answerMChoiceMapper.toAnswerMchoice(answerRequest);
                    answer.setUser(user);
                    answer.setQuestion(question);
                    answerMChoiceRepository.save(answer);

                    answerResponses.add(answerMChoiceMapper.answerMChoiceResponse(answer));
                }
            }

            // Trả response tổng hợp
            QuestionWithAnswersResponse response = new QuestionWithAnswersResponse();
            response.setQuestion(questionMapper.questionResponse(question));
            response.setAnswers(answerResponses);
            return response;
        }catch (AppException e) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
    }
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
    public Page<QuestionResponse> getQuestionByTopic(String topic, Pageable pageable,String token){
        User user = tokenService.getTeacherOrAdmin(token,true);
        QuestionTopic questionTopic = QuestionTopic.valueOf(topic.toUpperCase());
        Page<Question> questions = questionRepository.findByTopic(questionTopic,pageable);
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
