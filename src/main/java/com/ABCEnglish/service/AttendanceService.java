package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.AttendanceStudentRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.AttendanceResponse;
import com.ABCEnglish.dto.response.AttendanceStudentResponse;
import com.ABCEnglish.dto.response.ClassStudentResponse;
import com.ABCEnglish.entity.*;
import com.ABCEnglish.exception.AppException;
import com.ABCEnglish.exception.ErrorCode;
import com.ABCEnglish.mapper.AttendanceMapper;
import com.ABCEnglish.repository.AttendanceRepository;
import com.ABCEnglish.repository.ClassOfflineRepository;
import com.ABCEnglish.repository.ClassOfflineSessionRepository;
import com.ABCEnglish.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttendanceService {
    AuthenticationService authenticationService;
    UserRepository userRepository;
    private final ClassOfflineRepository classOfflineRepository;
    private final ClassStudentService classStudentService;
    private final ClassOfflineSessionRepository classOfflineSessionRepository;
    private final AttendanceMapper attendanceMapper;
    private final AttendanceRepository attendanceRepository;

    public List<AttendanceResponse> addAttendance(Integer classId, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        ClassOffline classOffline = classOfflineRepository.findById(classId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
        List<ClassOfflineSession> classOfflineSessions = classOfflineSessionRepository.findByClassOffline(classOffline);
        List<Attendance> attendances = new ArrayList<>();
        log.info("Fetching students for class: {}", classId);
        List<ClassStudentResponse> students = classStudentService.getAllStudentsOfClass(classId, token);
        log.info("Total students fetched: {}", students.size());
        for (ClassOfflineSession session : classOfflineSessions) {
            log.info("Processing session: {}", session.getSessionId());
            for (ClassStudentResponse studentResponse : students) {
                log.info("Processing student: {}", studentResponse.getUserId());
                User student = userRepository.findById(studentResponse.getUserId())
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
                Attendance attendance = Attendance.builder()
                        .session(session)
                        .student(student)
                        .attendance(AttendanceStatus.ABSENT)
                        .createdAt(new Date())
                        .updatedAt(new Date())
                        .build();
                attendances.add(attendance);
            }
        }
        attendanceRepository.saveAll(attendances);
        return attendances.stream()
                .map(AttendanceMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }

    public AttendanceStudentResponse updateStatusAttendance(Integer classId, AttendanceStudentRequest request,IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        ClassOffline classOffline = classOfflineRepository.findById(classId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
        Attendance attendance = attendanceRepository.findById(request.getAttendanceId())
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
        attendance.setAttendance(request.getAttendanceStatus());

        AttendanceStudentResponse attendanceStudentResponse = new AttendanceStudentResponse();
        attendanceStudentResponse.setAttendanceId(attendance.getAttendanceId());
        attendanceStudentResponse.setAttendanceStatus(request.getAttendanceStatus());
        attendanceRepository.save(attendance);
        return ApiResponse.<AttendanceStudentResponse>builder().result(attendanceStudentResponse).build().getResult();
    }

}
