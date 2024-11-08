package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.request.ResultExerciseRequest;
import com.ABCEnglish.dto.response.QuestionResponse;
import com.ABCEnglish.dto.response.ResultExerciseResponse;
import com.ABCEnglish.entity.ResultExercises;
import com.ABCEnglish.service.ResultExerciseService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/result")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResultExerciseController {
    ResultExerciseService resultExerciseService;
    @PostMapping("/{exerciseId}")
    public ResultExerciseResponse createResult(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                               @PathVariable Integer exerciseId) throws ParseException, JOSEException {
        String token =authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        ResultExerciseResponse resultExercises = resultExerciseService.createResult(exerciseId,introspectRequest);
        return ApiResponse.<ResultExerciseResponse>builder().result(resultExercises).build().getResult();
    }

    @GetMapping("/{resultId}")
    public ResultExerciseResponse getResult(@PathVariable Integer resultId,@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token =authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        ResultExerciseResponse result = resultExerciseService.getResult(resultId, introspectRequest);
        return ApiResponse.<ResultExerciseResponse>builder().result(result).build().getResult();
    }
}
