package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.ClassOfflineRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.ClassOfflineResponse;
import com.ABCEnglish.service.ClassOfflineService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
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
}
