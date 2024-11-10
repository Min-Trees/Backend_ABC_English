package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.ExerciseRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.DocDeleteResponse;
import com.ABCEnglish.dto.response.ExerciseDeleteResponse;
import com.ABCEnglish.dto.response.ExerciseResponse;
import com.ABCEnglish.dto.response.LessonResponse;
import com.ABCEnglish.entity.*;
import com.ABCEnglish.exceptioin.AppException;
import com.ABCEnglish.exceptioin.ErrorCode;
import com.ABCEnglish.mapper.ExerciseMapper;
import com.ABCEnglish.reponsesitory.ExerciseRepository;
import com.ABCEnglish.reponsesitory.LessonRepository;
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
public class ExerciseService {
    ExerciseRepository exerciseRepository;
    LessonRepository lessonRepository;
    ExerciseMapper exerciseMapper;
    UserRepository userRepository;
    AuthenticationService authenticationService;

    public ExerciseResponse createUser(Integer lessonId, ExerciseRequest request, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        // kiem tra su ton tai cua user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        // kiem tra su ton tai cua khoa hoc ( tai lieu thuoc khoa hoc )
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));

        Exercises exercises = exerciseMapper.toExercise(request);
        exercises.setCreator(user);
        exercises.setLesson(lesson);
        exerciseRepository.save(exercises);

        return exerciseMapper.exerciseResponse(exercises);
    }

    public ExerciseResponse updateExercise( Integer lessonId,Integer exerciseId, ExerciseRequest request,IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        // kiem tra su ton tai cua user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        // kiem tra su ton tai cua khoa hoc ( tai lieu thuoc khoa hoc )
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));
        Exercises exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND));

        exerciseMapper.updateExercises(request,exercise);
        Exercises updateExercise =  exerciseRepository.save(exercise);
        return exerciseMapper.exerciseResponse(updateExercise);
    }

    public Page<ExerciseResponse> getAllExercise(Pageable pageable){
        // thuc hien phan trang
        Page<Exercises> exercise = exerciseRepository.findAll(pageable);
        return exercise.map(exerciseMapper::exerciseResponse);
    }

    public ExerciseResponse getExercise(Integer lessonId, Integer exerciseId, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));

        Exercises exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND));
        return exerciseMapper.exerciseResponse(exercise);
    }

    public ExerciseDeleteResponse deleteExercise(Integer lessonId, Integer exerciseId, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));

        Exercises exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new AppException(ErrorCode.EXERCISE_NOT_FOUND));

        exerciseRepository.delete(exercise);
        ExerciseDeleteResponse exerciseDeleteResponse = new ExerciseDeleteResponse();
        exerciseDeleteResponse.setExerciseId(exerciseId);
        exerciseDeleteResponse.setStatus(false);
        exerciseDeleteResponse.setMessage("delete success");
        return ApiResponse.<ExerciseDeleteResponse>builder().result(exerciseDeleteResponse).build().getResult();
    }
}
