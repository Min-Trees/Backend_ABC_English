package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.request.TestRequest;
import com.ABCEnglish.dto.response.CourseResponse;
import com.ABCEnglish.dto.response.TestResponse;
import com.ABCEnglish.service.TestService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestController {
    @Autowired
    private TestService testService;
    @GetMapping()
    public ResponseEntity<Page<TestResponse>> getTest(Pageable pageable){
        Page<TestResponse> responses = testService.findAll(pageable);
        return ResponseEntity.ok(responses);
    }
    @PostMapping()
    public ApiResponse<TestResponse> addTest(
            @RequestBody TestRequest testRequest,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader)
            throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        TestResponse result=testService.createTest(testRequest,introspectRequest);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(result);
        apiResponse.setMessage("Success");
        apiResponse.setCode(1000);
        return apiResponse;
    }
    @PutMapping("/{testId}")
    public ApiResponse<TestResponse> updateTest(
            @RequestBody TestRequest testRequest,
            @PathVariable Integer testId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        TestResponse result = testService.updateTest(testId,testRequest,introspectRequest);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(result);
        apiResponse.setMessage("Success");
        apiResponse.setCode(1000);
        return apiResponse;
    }
    @DeleteMapping("/{testId}")
    public ApiResponse<TestResponse> deleteTest(
            @PathVariable Integer testId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        String result = testService.deleteTest(testId,introspectRequest);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(result);
        apiResponse.setMessage("Success");
        apiResponse.setCode(1000);
        return apiResponse;
    }
}
