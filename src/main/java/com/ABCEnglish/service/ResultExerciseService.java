package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.request.ResultExerciseRequest;
import com.ABCEnglish.dto.response.ResultExerciseResponse;
import com.ABCEnglish.entity.Exercises;
import com.ABCEnglish.entity.Question;
import com.ABCEnglish.entity.ResultExercises;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exceptioin.AppException;
import com.ABCEnglish.exceptioin.ErrorCode;
import com.ABCEnglish.mapper.ResultExerciseMapper;
import com.ABCEnglish.reponsesitory.ExerciseRepository;
import com.ABCEnglish.reponsesitory.QuestionRepository;
import com.ABCEnglish.reponsesitory.ResultExerciseRepository;
import com.ABCEnglish.reponsesitory.UserRepository;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResultExerciseService {
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    ResultExerciseMapper resultExerciseMapper;
    @Autowired
    ResultExerciseRepository resultExerciseRepository;
    @Autowired
    ExerciseRepository exerciseRepository;
    public ResultExerciseResponse createResult(ResultExerciseRequest request, Integer exerciseId, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        // Kiểm tra người dùng tồn tại
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Exercises exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND));
        List<Question> allQuestions = questionRepository.findByExercise_ExerciseId(exerciseId);
        Double totalScore = questionRepository.getTotalScoreByExerciseId(exerciseId);

        ResultExercises result = resultExerciseMapper.toResult(request);
        result.setScore(totalScore);
        result.setUser(user);
        result.setExercise(exercise);
        result.setCreatedAt(new Date());
        result.setUpdatedAt(new Date());

        resultExerciseRepository.save(result);
        return resultExerciseMapper.resultResponse(result);
    }
    public ResultExerciseResponse getResult(Integer resultId,IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        // Kiểm tra người dùng tồn tại
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        ResultExercises response = resultExerciseRepository.findById(resultId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return resultExerciseMapper.resultResponse(response);
    }
}
