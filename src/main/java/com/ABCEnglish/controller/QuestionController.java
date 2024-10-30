package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.request.QuestionRequest;
import com.ABCEnglish.dto.response.QuestionDeleteResponse;
import com.ABCEnglish.dto.response.QuestionResponse;
import com.ABCEnglish.service.QuestionService;
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
@RequestMapping("/api/v1/question")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionController {
    QuestionService questionService;
    @PostMapping("/{exerciseId}")
    public QuestionResponse addQuestion(
            @PathVariable  Integer exerciseId,
            @RequestBody QuestionRequest questionRequest,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {

        String token =authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        QuestionResponse result =  questionService.createQuestion(exerciseId,questionRequest,introspectRequest);
        return ApiResponse.<QuestionResponse>builder().result(result).build().getResult();

    }
    @PutMapping("/{exerciseId}/{questionId}")
    public QuestionResponse updateQuestion(
            @PathVariable Integer exerciseId,
            @PathVariable Integer questionId,
            @RequestBody QuestionRequest questionRequest,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token =authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        QuestionResponse result = questionService.updateQuestion(exerciseId,questionId,questionRequest,introspectRequest);
    return ApiResponse.<QuestionResponse>builder().result(result).build().getResult();
    }

    @GetMapping()
    public ResponseEntity<Page<QuestionResponse>> getAllQuestions(Pageable pageable){
        Page<QuestionResponse> result = questionService.getAllQuestion(pageable);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{exerciseId}/{questionId}")
    public QuestionDeleteResponse deleteResponse(
            @PathVariable Integer exerciseId,
            @PathVariable Integer questionId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token =authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        return questionService.deleteQuestion(exerciseId,questionId,introspectRequest);
    }
}
