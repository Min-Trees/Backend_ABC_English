package com.ABCEnglish.controller;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.AttendanceResponse;
import com.ABCEnglish.service.AttendanceService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/class/attendance")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping("/{classId}")
    public List<ApiResponse<AttendanceResponse>> addAttendance(
            @PathVariable Integer classId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) throws ParseException, JOSEException {
        String token = authorizationHeader.substring("Bearer".length());
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        List<AttendanceResponse> attendanceResponses = attendanceService.addAttendance(classId,introspectRequest);
        return attendanceResponses.stream().map(ApiResponse::new).collect(Collectors.toList());
    }

}
