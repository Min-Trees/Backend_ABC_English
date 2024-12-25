package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.CourseRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.CourseDeleteResponse;
import com.ABCEnglish.dto.response.CourseResponse;
import com.ABCEnglish.entity.Course;
import com.ABCEnglish.service.CourseService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseController {
    CourseService courseService;
    @PostMapping()
    public ApiResponse<CourseResponse> addCourse(
            @RequestBody CourseRequest request,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader
            ) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        System.out.println(token);
        CourseResponse result = courseService.createCourse(request,introspectRequest);
        return ApiResponse.<CourseResponse>builder().result(result).build();
    }
    @PutMapping("/{courseId}")
    public ApiResponse<CourseResponse> updateCourse(
            @PathVariable Integer courseId,
            @RequestBody CourseRequest request,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader
            ) throws ParseException, JOSEException {
        //Kiem tra token
        String token = authorizationHeader.substring("Bearer".length());
        // tao token moi
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        CourseResponse result = courseService.updateCourse(courseId,request,introspectRequest);
        return ApiResponse.<CourseResponse>builder().result(result).build();
    }

    @GetMapping()
    public ResponseEntity<Page<CourseResponse>> getAllCourse(Pageable pageable){
        Page<CourseResponse> courseResponses = courseService.getAllCourse(pageable);
        return ResponseEntity.ok(courseResponses);
    }

    @GetMapping("/{courseId}")
    public ApiResponse<CourseResponse> getCourse(
            @PathVariable Integer courseId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        // tao token moi
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        CourseResponse result = courseService.getCourse(courseId,introspectRequest);
        return ApiResponse.<CourseResponse>builder().result(result).build();
    }

    @DeleteMapping("/{courseId}")
    public CourseDeleteResponse deleteCourseResponse(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer courseId) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        return courseService.deleteCourse(courseId, introspectRequest);
    }

    @GetMapping("/user/courses")
    public Page<CourseResponse> getUserCourses(
            @RequestBody IntrospectRequest token,
            Pageable pageable) throws Exception {
        return courseService.getCoursesByUser(token, pageable);
    }

}
