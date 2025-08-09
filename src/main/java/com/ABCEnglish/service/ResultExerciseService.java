package com.ABCEnglish.service;

import com.ABCEnglish.dto.response.ResultExerciseResponse;
import com.ABCEnglish.entity.Exercises;
import com.ABCEnglish.entity.ResultExercises;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exception.AppException;
import com.ABCEnglish.exception.ErrorCode;
import com.ABCEnglish.mapper.ResultExerciseMapper;
import com.ABCEnglish.repository.ExerciseRepository;
import com.ABCEnglish.repository.QuestionRepository;
import com.ABCEnglish.repository.ResultExerciseRepository;
import com.ABCEnglish.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private TokenService tokenService;

    public ResultExerciseResponse createResult( Integer exerciseId, String token) {
        User user = tokenService.getUserFromToken(token);
        Exercises exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND));
        Double totalScore = questionRepository.getTotalScoreByExerciseId(exerciseId);

        ResultExercises result = resultExerciseMapper.toResult(exerciseId);
        result.setScore(totalScore);
        result.setUser(user);
        result.setExercise(exercise);
        result.setCreatedAt(new Date());
        result.setUpdatedAt(new Date());

        resultExerciseRepository.save(result);
        return resultExerciseMapper.resultResponse(result);
    }
    public ResultExerciseResponse getResult(Integer resultId,String token) throws ParseException, JOSEException {
        User user = tokenService.getUserFromToken(token);
        ResultExercises response = resultExerciseRepository.findById(resultId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return resultExerciseMapper.resultResponse(response);
    }

    public List<ResultExerciseResponse> getResultsByExercise(Integer exerciseId, String token) {
        User user = tokenService.getUserFromToken(token);
        List<ResultExercises> results = resultExerciseRepository
                .findByExercise_ExerciseIdAndUser_UserId(exerciseId, user.getUserId());
        return results.stream()
                .map(resultExerciseMapper::resultResponse)
                .collect(Collectors.toList());
    }
}
