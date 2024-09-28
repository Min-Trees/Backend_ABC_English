package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.DocRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.request.LessonRequest;
import com.ABCEnglish.dto.response.DocDeleteResponse;
import com.ABCEnglish.dto.response.DocResponse;
import com.ABCEnglish.dto.response.LessonDeleteResponse;
import com.ABCEnglish.dto.response.LessonResponse;
import com.ABCEnglish.service.LessonService;
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
@RequestMapping("/api/v1/lesson")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LessonController {
    LessonService lessonService;

    @PostMapping("/{courseId}")
    public ApiResponse<LessonResponse> addLesson(
            @RequestBody LessonRequest request,
            @PathVariable Integer courseId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException{

        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        LessonResponse result = lessonService.createdLesson(courseId,request,introspectRequest);
        return ApiResponse.<LessonResponse>builder().result(result).build();

    }

    @PutMapping("/{courseId}/{lessonId}")
    public ApiResponse<LessonResponse> updateLesson(
            @RequestBody LessonRequest request,
            @PathVariable Integer courseId,
            @PathVariable Integer lessonId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException{
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);

        LessonResponse result = lessonService.updateLesson(courseId,lessonId,request,introspectRequest);
        return ApiResponse.<LessonResponse>builder().result(result).build();
    }

    @GetMapping()
    public ResponseEntity<Page<LessonResponse>> getAllLesson(Pageable pageable){
        Page<LessonResponse> lessonResponses = lessonService.getAllLesson(pageable);
        return ResponseEntity.ok(lessonResponses);
    }

    @DeleteMapping("/{courseId}/{lessonId}")
    public LessonDeleteResponse deleteLessonResponse(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer lessonId,
            @PathVariable Integer courseId) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        return lessonService.deleteLesson(courseId,lessonId, introspectRequest);
    }
}
