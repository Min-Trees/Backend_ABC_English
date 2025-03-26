package com.ABCEnglish.controller;

import com.ABCEnglish.configuration.JwtUtil;
import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.CourseRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.CourseDeleteResponse;
import com.ABCEnglish.dto.response.CourseResponse;
import com.ABCEnglish.entity.Course;
import com.ABCEnglish.exception.AppException;
import com.ABCEnglish.exception.ErrorCode;
import com.ABCEnglish.service.CourseService;
import com.ABCEnglish.service.TokenService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    JwtUtil jwtUtil;
    TokenService tokenService;
    @PostMapping()
    public ApiResponse<CourseResponse> addCourse(
            @RequestBody CourseRequest request,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        logger.info("Received request to add course: {}", request);

        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring("Bearer ".length())
                : authorizationHeader;
        try {
            CourseResponse result = courseService.createCourse(request, token);
            logger.info("Course created successfully: {}", result);
            return ApiResponse.<CourseResponse>builder().result(result).build();
        } catch (AppException e) {
            logger.error("Failed to create course: {}", e.getMessage(), e);
            throw e; // Ném lại lỗi từ CourseService (ACCESS_DENIED, USER_NOT_EXISTED, v.v.)
        } catch (Exception e) {
            logger.error("Unexpected error while creating course: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.COURSE_CREATION_FAILED);
        }
    }
    @PutMapping("/{courseId}")
    public ApiResponse<CourseResponse> updateCourse(
            @PathVariable Integer courseId,
            @RequestBody CourseRequest request,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader
            ) throws ParseException, JOSEException {
        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring("Bearer ".length()).trim()
                : authorizationHeader;
        try {
            CourseResponse result = courseService.updateCourse(courseId,request, token);
            logger.info("Course created successfully: {}", result);
            return ApiResponse.<CourseResponse>builder().result(result).build();
        } catch (AppException e) {
            logger.error("Failed to create course: {}", e.getMessage(), e);
            throw e; // Ném lại lỗi từ CourseService (ACCESS_DENIED, USER_NOT_EXISTED, v.v.)
        } catch (Exception e) {
            logger.error("Unexpected error while creating course: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.COURSE_CREATION_FAILED);
        }
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
        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring("Bearer ".length()).trim()
                : authorizationHeader;
        CourseResponse result = courseService.getCourse(courseId,token);
        return ApiResponse.<CourseResponse>builder().result(result).build();
    }

    @DeleteMapping("/{courseId}")
    public CourseDeleteResponse deleteCourseResponse(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Integer courseId) throws ParseException, JOSEException {
        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring("Bearer ".length()).trim()
                : authorizationHeader;
        return courseService.deleteCourse(courseId, token);
    }

    @GetMapping("/user/courses")
    public Page<CourseResponse> getUserCourses(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader,
            Pageable pageable) throws Exception {
        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring("Bearer ".length()).trim()
                : authorizationHeader;
        return courseService.getCoursesByUser(token, pageable);
    }

}
