package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.request.ResultTestRequest;
import com.ABCEnglish.service.ResultTestService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/resulttest")
@RequiredArgsConstructor
public class ResultTestController {
    @Autowired
    private ResultTestService resultTestService;

    @PostMapping("")
    public ApiResponse create(
            @RequestBody ResultTestRequest resultTestRequest,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("success")
                .result(resultTestService.create(resultTestRequest,introspectRequest))
                .build();
        return apiResponse;
    }
    @DeleteMapping("/{testId}")
    public ApiResponse delete(@PathVariable Integer testId, @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("success")
                .result(resultTestService.delete(testId,introspectRequest))
                .build();
        return apiResponse;
    }
}
