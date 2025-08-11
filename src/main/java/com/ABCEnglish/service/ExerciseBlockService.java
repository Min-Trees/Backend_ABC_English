package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.ExerciseBlockQuestionRequest;
import com.ABCEnglish.dto.request.ExerciseBlockRequest;
import com.ABCEnglish.dto.response.*;
import com.ABCEnglish.entity.*;
import com.ABCEnglish.exception.AppException;
import com.ABCEnglish.exception.ErrorCode;
import com.ABCEnglish.mapper.ExerciseBlockMapper;
import com.ABCEnglish.mapper.QuestionMapper;
import com.ABCEnglish.repository.ExerciseBlockQuestionRepository;
import com.ABCEnglish.repository.ExerciseBlockRepository;
import com.ABCEnglish.repository.ExerciseRepository;
import com.ABCEnglish.repository.QuestionRepository;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExerciseBlockService {
    private final TokenService tokenService;
    private final ExerciseRepository exerciseRepository;
    private final ExerciseBlockMapper exerciseBlockMapper;
    private final ExerciseBlockRepository exerciseBlockRepository;
    private final QuestionRepository questionRepository;
    private final ExerciseBlockQuestionRepository exerciseBlockQuestionRepository;
    private final QuestionMapper questionMapper;
    private final AnswerMChoiceService answerMChoiceService;

    public ExerciseBlockResponse createBlock(Integer exerciseId, ExerciseBlockRequest request, String token) throws ParseException, JOSEException {
        User user = tokenService.getTeacherOrAdmin(token, true);
        Exercises exercises = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND));
        ExerciseBlock block = exerciseBlockMapper.toEntity(request);
        block.setExercise(exercises);
        exerciseBlockRepository.save(block);
        return exerciseBlockMapper.toResponse(block);

    }

    public String deleteBlock(Integer exerciseId, Integer blockId, String token) throws ParseException, JOSEException {
        User user = tokenService.getTeacherOrAdmin(token, true);
        boolean exists = exerciseBlockRepository.existsByExerciseBlockIdAndExercise_ExerciseId(blockId, exerciseId);
        if (!exists) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }

        ExerciseBlock exerciseBlock = exerciseBlockRepository.findById(blockId)
                .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND));

        exerciseBlockRepository.delete(exerciseBlock);
        return "deleted successfully";
    }

    public List<ExerciseBlockResponse> getAllByOrderByOrderIndexAsc(Integer exerciseId, String token) throws ParseException, JOSEException {
        User user = tokenService.getTeacherOrAdmin(token, true);
        Exercises exercises = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND));

        return exerciseBlockRepository.findByExercise_ExerciseIdOrderByOrderIndexAsc(exerciseId)
                .stream()
                .map(exerciseBlockMapper::toResponse)
                .collect(Collectors.toList());
    }

    public String addQuestionToBlock(ExerciseBlockQuestionRequest request, String token){
        User user = tokenService.getTeacherOrAdmin(token, true);
        ExerciseBlock exerciseBlock = exerciseBlockRepository.findById(request.getExerciseBlockId())
                .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND));
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
        ExerciseBlockQuestion exerciseBlockQuestion = new ExerciseBlockQuestion();
        exerciseBlockQuestion.setExerciseBlock(exerciseBlock);
        exerciseBlockQuestion.setQuestion(question);
        exerciseBlockQuestion.setOrderIndex(request.getOrderIndex());
        exerciseBlockQuestionRepository.save(exerciseBlockQuestion);
        return "success";
    }
    public String deleteQuestionFromBlock(Integer exerciseBlockId, Integer exerciseBlockQuestionId, String token) {
        User user = tokenService.getTeacherOrAdmin(token, true);

        boolean isRelated = exerciseBlockQuestionRepository
                .existsByExerciseBlockQuestionIdAndExerciseBlock_ExerciseBlockId(exerciseBlockQuestionId, exerciseBlockId);
        System.out.println("isRelated = " + isRelated);
        if (!isRelated) {
            throw new AppException(ErrorCode.SOCIAL_NOT_FOUND); // Tránh xoá nhầm data không thuộc về block
        }

        ExerciseBlockQuestion exerciseBlockQuestion = exerciseBlockQuestionRepository
                .findById(exerciseBlockQuestionId)
                .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND));

        exerciseBlockQuestionRepository.delete(exerciseBlockQuestion);
        return "Delete success";
    }

    public List<ExerciseBlockQuestionResponse> getQuestionsById(Integer exerciseBlockId, String token) {
        // Xác thực người dùng
        User user = tokenService.getUserFromToken(token);

        // Tìm ExerciseBlock
        ExerciseBlock exerciseBlock = exerciseBlockRepository.findById(exerciseBlockId)
                .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND));

        // Lấy danh sách ExerciseBlockQuestion
        List<ExerciseBlockQuestion> exerciseBlockQuestions = exerciseBlockQuestionRepository
                .findByExerciseBlock_ExerciseBlockIdOrderByOrderIndexAsc(exerciseBlockId);

        // Map sang DTO
        List<ExerciseBlockQuestionResponse> responses = new ArrayList<>();
        for (ExerciseBlockQuestion exerciseBlockQuestion : exerciseBlockQuestions) {
            Question question = exerciseBlockQuestion.getQuestion();
            List<AnswerMChoiceResponse> answerMChoices = answerMChoiceService.getAllAnswersMChoice(question.getQuestionId());
            // Map Question entity sang QuestionResponse DTO
            QuestionWithAnswersResponse questionResponse = new QuestionWithAnswersResponse();
            questionResponse.setQuestion(questionMapper.questionResponse(question));
            questionResponse.setAnswers(answerMChoices);

            // Set thêm trường nếu cần

            // Tạo response
            ExerciseBlockQuestionResponse response = new ExerciseBlockQuestionResponse();
            response.setExerciseBlockQuestionId(exerciseBlockQuestion.getExerciseBlockQuestionId());
            response.setExerciseBlockId(exerciseBlockId);
            response.setQuestion(questionResponse);
            response.setOrderIndex(exerciseBlockQuestion.getOrderIndex());

            responses.add(response);
        }

        return responses;
    }
}
