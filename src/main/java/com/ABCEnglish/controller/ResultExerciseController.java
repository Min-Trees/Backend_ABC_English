package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.response.ResultExerciseResponse;
import com.ABCEnglish.exception.AppException;
import com.ABCEnglish.exception.ErrorCode;
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
                                               @PathVariable Integer exerciseId) {
        try {
            String token = authorizationHeader.startsWith("Bearer")
                    ? authorizationHeader.substring("Bearer".length()).trim()
                    : authorizationHeader;
            ResultExerciseResponse resultExercises = resultExerciseService.createResult(exerciseId, token);
            return ApiResponse.<ResultExerciseResponse>builder().result(resultExercises).build().getResult();
        } catch (AppException e){
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
    }

    @GetMapping("/{resultId}")
    public ResultExerciseResponse getResult(@PathVariable Integer resultId,@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        try {
            String token = authorizationHeader.startsWith("Bearer")
                    ? authorizationHeader.substring("Bearer".length()).trim()
                    : authorizationHeader;
            ResultExerciseResponse result = resultExerciseService.getResult(resultId, token);
            return ApiResponse.<ResultExerciseResponse>builder().result(result).build().getResult();
        } catch (AppException e){
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
    }
}
