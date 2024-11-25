package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.ExerciseRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.ExerciseDeleteResponse;
import com.ABCEnglish.dto.response.ExerciseResponse;
import com.ABCEnglish.service.ExerciseService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/api/v1/exercise")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExerciseController {
    ExerciseService exerciseService;
    @PostMapping("/{lessonId}")
    ApiResponse<ExerciseResponse> addExercise(
            @PathVariable Integer lessonId,
            @RequestBody ExerciseRequest request,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader
            ) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        ExerciseResponse result = exerciseService.createUser(lessonId,request,introspectRequest);
        return ApiResponse.<ExerciseResponse>builder().result(result).build();
    }

    @PutMapping("/{lessonId}/{exerciseId}")
    ApiResponse<ExerciseResponse> updateExercise(
            @PathVariable Integer lessonId,
            @PathVariable Integer exerciseId,
            @RequestBody ExerciseRequest request,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) throws ParseException, JOSEException{
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        ExerciseResponse result = exerciseService.updateExercise(lessonId,exerciseId,request,introspectRequest);
        return ApiResponse.<ExerciseResponse>builder().result(result).build();
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<Page<ExerciseResponse>> getAllExercises(Pageable pageable, @PathVariable Integer lessonId){
        Page<ExerciseResponse> result = exerciseService.getAllExercise(pageable, lessonId);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/{lessonId}/{exerciseId}")
    ApiResponse<ExerciseResponse> getExercise(
            @PathVariable Integer lessonId,
            @PathVariable Integer exerciseId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) throws ParseException, JOSEException{
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        ExerciseResponse result = exerciseService.getExercise(lessonId,exerciseId,introspectRequest);
        return ApiResponse.<ExerciseResponse>builder().result(result).build();
    }

    @DeleteMapping("/{lessonId}/{exerciseId}")
    public ExerciseDeleteResponse deleteExercise(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer lessonId,
            @PathVariable Integer exerciseId) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        return exerciseService.deleteExercise(lessonId,exerciseId,introspectRequest);
    }
}
