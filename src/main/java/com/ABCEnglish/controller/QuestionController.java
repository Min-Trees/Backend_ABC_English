package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.request.QuestionRequest;
import com.ABCEnglish.dto.request.QuestionWithAnswersRequest;
import com.ABCEnglish.dto.response.QuestionDeleteResponse;
import com.ABCEnglish.dto.response.QuestionResponse;
import com.ABCEnglish.dto.response.QuestionWithAnswersResponse;
import com.ABCEnglish.exception.AppException;
import com.ABCEnglish.service.QuestionService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/api/v1/question")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionController {
    QuestionService questionService;
//    @PostMapping("/{exerciseId}")
//    public QuestionResponse addQuestion(
//            @PathVariable  Integer exerciseId,
//            @RequestBody QuestionRequest questionRequest,
//            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
//
//        String token = authorizationHeader.startsWith("Bearer")
//                ? authorizationHeader.substring("Bearer".length()).trim()
//                : authorizationHeader;
//        QuestionResponse result =  questionService.createQuestion(exerciseId,questionRequest,token);
//        return ApiResponse.<QuestionResponse>builder().result(result).build().getResult();
//
//    }
    @PostMapping("/{exerciseId}")
    public ResponseEntity<QuestionWithAnswersResponse> createQuestion(
            @PathVariable Integer exerciseId,
            @RequestBody QuestionWithAnswersRequest questionRequest,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        String token = authorizationHeader.startsWith("Bearer ") ?
                authorizationHeader.substring(7) : authorizationHeader;

        QuestionWithAnswersResponse result = questionService.createQuestionWithAnswer(exerciseId, questionRequest, token);
        return ResponseEntity.ok(result);
    }
    @PutMapping("/{exerciseId}/{questionId}")
    public QuestionResponse updateQuestion(
            @PathVariable Integer exerciseId,
            @PathVariable Integer questionId,
            @RequestBody QuestionRequest questionRequest,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.startsWith("Bearer")
                ? authorizationHeader.substring("Bearer".length()).trim()
                : authorizationHeader;
        QuestionResponse result = questionService.updateQuestion(exerciseId,questionId,questionRequest,token);
    return ApiResponse.<QuestionResponse>builder().result(result).build().getResult();
    }

//    @GetMapping("/{exerciseId}")
//    public ResponseEntity<Page<QuestionResponse>> getAllQuestions(Pageable pageable, @PathVariable Integer exerciseId){
//        Page<QuestionResponse> result = questionService.getAllQuestion(pageable, exerciseId);
//        return ResponseEntity.ok(result);
//    }

    @GetMapping("/get-question/{questionId}")
    public ResponseEntity<?> getQuestion(
            @PathVariable Integer questionId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        try {
            String token = authorizationHeader.startsWith("Bearer ") ?
                    authorizationHeader.substring(7) : authorizationHeader;

            QuestionWithAnswersResponse response = questionService.getQuestionWithAnswer(questionId, token);
            return ResponseEntity.ok(response);

        } catch (AppException e) {
            // Ghi log lỗi rõ ràng
            System.err.println("Handled AppException: " + e.getErrorCode());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getErrorCode().name()));

        } catch (Exception e) {
            // Lỗi hệ thống
            System.err.println("Unhandled error: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "INTERNAL_SERVER_ERROR"));
        }
    }


//    @GetMapping("/{exerciseId}/{questionId}")
//    public QuestionResponse getLesson(
//            @PathVariable Integer exerciseId,
//            @PathVariable Integer questionId,
//            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
//        String token =authorizationHeader.substring("Bearer".length());
//        IntrospectRequest introspectRequest = new IntrospectRequest();
//        introspectRequest.setToken(token);
//        QuestionResponse result = questionService.getAllQuestion()
//        return ApiResponse.<QuestionResponse>builder().result(result).build().getResult();
//    }

    @DeleteMapping("/{exerciseId}/{questionId}")
    public QuestionDeleteResponse deleteResponse(
            @PathVariable Integer exerciseId,
            @PathVariable Integer questionId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.startsWith("Bearer ") ?
                authorizationHeader.substring(7) : authorizationHeader;
        return questionService.deleteQuestion(exerciseId,questionId,token);
    }
}
