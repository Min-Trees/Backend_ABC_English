package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.ClassStudentRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.ClassStudentResponse;
import com.ABCEnglish.repository.ClassStudentRepository;
import com.ABCEnglish.service.ClassStudentService;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/class-student")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassStudentController {

    private final ClassStudentService classStudentService;
    private final ClassStudentRepository classStudentRepository;

    @PostMapping("/add")
    public ApiResponse<ClassStudentResponse> addStudent(
            @RequestBody ClassStudentRequest request,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader ) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        ClassStudentResponse result = classStudentService.addClass(request,introspectRequest);
        return ApiResponse.<ClassStudentResponse>builder().result(result).build();

    }

    @PutMapping("/update")
    public ApiResponse<ClassStudentResponse> updateStudent(
            @RequestBody ClassStudentRequest request,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        ClassStudentResponse result = classStudentService.updateClass(request, introspectRequest);
        return ApiResponse.<ClassStudentResponse>builder().result(result).build();

    }

    @DeleteMapping()
    public ApiResponse<String> deleteStudentByClassId(
            @RequestBody ClassStudentRequest request,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        String message = classStudentService.deleteStudentOfClass(request,introspectRequest);
        return ApiResponse.<String>builder().result(message).build();
    }
// get student to list<ClassStudentResponse> make attend for every student
    @GetMapping("/get-all-student/{classId}")
    public List<ApiResponse<ClassStudentResponse>> getAllStudentsByClassId(
            @PathVariable Integer classId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        List<ClassStudentResponse> classStudentResponses = classStudentService.getAllStudentsOfClass(classId, introspectRequest);
        return classStudentResponses.stream().map(ApiResponse::new).collect(Collectors.toList());
    }
}
