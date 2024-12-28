package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.ClassOfflineRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.ClassOfflineDeleteResponse;
import com.ABCEnglish.dto.response.ClassOfflineResponse;
import com.ABCEnglish.service.ClassOfflineService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/class")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassOfflineController {
    ClassOfflineService classOfflineService;
    @PostMapping("{courseId}")
    public ApiResponse<ClassOfflineResponse> createClassOffline(
            @PathVariable Integer courseId,
            @RequestBody ClassOfflineRequest request,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader ) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        ClassOfflineResponse result = classOfflineService.addClass(request,courseId,introspectRequest);
        return ApiResponse.<ClassOfflineResponse>builder().result(result).build();
    }

    @PutMapping("/update/{classId}")
    public ApiResponse<ClassOfflineResponse> updateClassOffline(
            @PathVariable Integer classId,
            @RequestBody ClassOfflineRequest request,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);

        ClassOfflineResponse result = classOfflineService.updateClassOffline(request,classId,introspectRequest);
        return ApiResponse.<ClassOfflineResponse>builder().result(result).build();
    }

    @GetMapping("/{classId}")
    public ApiResponse<ClassOfflineResponse> getClass(
            @PathVariable Integer classId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        ClassOfflineResponse result = classOfflineService.getClass(classId,introspectRequest);
        return ApiResponse.<ClassOfflineResponse>builder().result(result).build();
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Page<ClassOfflineResponse>> getAllClass(
            @PathVariable Integer courseId,
            Pageable pageable,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        Page<ClassOfflineResponse> classList = classOfflineService.getAllClass(courseId,pageable,introspectRequest);
        return ResponseEntity.ok(classList);
    }

    @DeleteMapping("/{classId}")
    public ClassOfflineDeleteResponse deleteClassOffline(
            @PathVariable Integer classId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        return classOfflineService.deleteClassOffline(classId, introspectRequest);
    }

}
