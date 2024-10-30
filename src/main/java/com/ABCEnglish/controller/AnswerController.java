package com.ABCEnglish.controller;
import com.ABCEnglish.dto.request.AnswerEssayRequest;
import com.ABCEnglish.dto.request.AnswerMChoiceRequest;
import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.AnswerEssayResponse;
import com.ABCEnglish.dto.response.AnswerMChoiceDeleteResponse;
import com.ABCEnglish.dto.response.AnswerMChoiceResponse;
import com.ABCEnglish.service.AnswerEssayService;
import com.ABCEnglish.service.AnswerMChoiceService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/api/v1/answer")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AnswerController {
    AnswerMChoiceService answerMChoiceService;
    AnswerEssayService answerEssayService;
    @PostMapping("/{questionId}")
    public ApiResponse<AnswerMChoiceResponse> addAnswerMChoice(
            @RequestBody AnswerMChoiceRequest request,
            @PathVariable Integer questionId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {

        String token = authorizationHeader.substring("Bearer".length());
        System.out.println(token);
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        AnswerMChoiceResponse result = answerMChoiceService.createAnswerMChoice(questionId, request, introspectRequest);
        return ApiResponse.<AnswerMChoiceResponse>builder().result(result).build();
    }

    @PutMapping("/{questionId}/{answerId}")
    public ApiResponse<AnswerMChoiceResponse> updateAnswerMChoice(
            @RequestBody AnswerMChoiceRequest request,
            @PathVariable Integer questionId,
            @PathVariable Integer answerId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {

        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        AnswerMChoiceResponse result = answerMChoiceService.updateAnswerMChoice(questionId, answerId, request, introspectRequest);

        return ApiResponse.<AnswerMChoiceResponse>builder().result(result).build();
    }

    @GetMapping("/{questionId}/{answerId}")
    public ApiResponse<AnswerMChoiceResponse> getAnswerMChoice(
            @PathVariable Integer questionId,
            @PathVariable Integer answerId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException{

        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        AnswerMChoiceResponse result = answerMChoiceService.getAnswerMChoice(questionId,answerId,introspectRequest);
        return ApiResponse.<AnswerMChoiceResponse>builder().result(result).build();
    }

    @DeleteMapping("/{questionId}/{answerId}")
    public AnswerMChoiceDeleteResponse deleteAnswerMChoice(
            @PathVariable Integer questionId,
            @PathVariable Integer answerId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {

        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        return answerMChoiceService.deleteAnswerMChoiceResponse(questionId,answerId,introspectRequest);
    }

    @PostMapping("/{questionId}/submit")
    public AnswerEssayResponse submitAnswer(
            @PathVariable Integer questionId,
            @RequestBody AnswerEssayRequest request,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, IOException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        return answerEssayService.createAnswerEssay(questionId, request, introspectRequest);
    }
}

