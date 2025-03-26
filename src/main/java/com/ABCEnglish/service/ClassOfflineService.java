package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.ClassOfflineRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.ClassOfflineDeleteResponse;
import com.ABCEnglish.dto.response.ClassOfflineResponse;
import com.ABCEnglish.entity.*;
import com.ABCEnglish.exception.AppException;
import com.ABCEnglish.exception.ErrorCode;
import com.ABCEnglish.mapper.ClassOfflineMapper;
import com.ABCEnglish.repository.ClassOfflineRepository;
import com.ABCEnglish.repository.ClassOfflineSessionRepository;
import com.ABCEnglish.repository.CourseRepository;
import com.ABCEnglish.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.DayOfWeek; // Import từ java.time
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassOfflineService {
    AuthenticationService authenticationService;
    UserRepository userRepository;
    CourseRepository courseRepository;
    ClassOfflineMapper classOfflineMapper;
    ClassOfflineRepository classOfflineRepository;
    ClassOfflineSessionRepository classOfflineSessionRepository;
    private final TokenService tokenService;

    public ClassOfflineResponse addClass(ClassOfflineRequest request, Integer courseId, String token)
            throws ParseException, JOSEException {
        User user = tokenService.getAdmin(token, true);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        try {
            log.info("Request received: {}", request);

            if (request.getStartDate() == null || request.getEndDate() == null) {
                log.error("StartDate or EndDate is null");
                throw new AppException(ErrorCode.INVALID_DATE_RANGE);
            }
            if (!request.getEndDate().after(request.getStartDate())) {
                log.error("EndDate is not after StartDate - startDate: {}, endDate: {}",
                        request.getStartDate(), request.getEndDate());
                throw new AppException(ErrorCode.INVALID_DATE_RANGE);
            }

            List<DayOfWeek> dayOfWeekList = request.getDayOfWeekList().stream()
                    .map(day -> DayOfWeek.valueOf(day.toUpperCase())) // Sử dụng java.time.DayOfWeek
                    .collect(Collectors.toList());

            log.info("Parsed data - startDate: {}, endDate: {}, dayOfWeekList: {}",
                    request.getStartDate(), request.getEndDate(), dayOfWeekList);

            ClassOffline classOffline = classOfflineMapper.toClassOffline(request);
            classOffline.setCourse(course);
            classOffline.setCreatedAt(new Date());
            classOffline.setUpdatedAt(new Date());
            classOffline.setDayOfWeekList(dayOfWeekList);

            classOfflineRepository.save(classOffline);

            ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
            LocalDate startDate = classOffline.getStartDate().toInstant().atZone(zoneId).toLocalDate();
            LocalDate endDate = classOffline.getEndDate().toInstant().atZone(zoneId).toLocalDate();

            List<ClassOfflineSession> newSessions = generateSessions(classOffline, startDate, endDate, dayOfWeekList);
            classOffline.getSessions().clear();
            classOffline.getSessions().addAll(newSessions);
            classOffline.setQuantitySession(classOffline.getSessions().size());

            classOfflineRepository.save(classOffline);

            return classOfflineMapper.classOfflineResponse(classOffline);

        } catch (IllegalArgumentException ex) {
            log.error("Invalid day of week: {}", ex.getMessage(), ex);
            throw new AppException(ErrorCode.UNEXPECTED_ERROR);
        } catch (AppException ex) {
            log.error("Error while adding class: {}", ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error occurred while adding class: {}", ex.getMessage(), ex);
            throw new AppException(ErrorCode.UNEXPECTED_ERROR);
        }
    }

    public ClassOfflineResponse updateClassOffline(ClassOfflineRequest request, Integer classId, String token)
            throws ParseException, JOSEException {
        User user = tokenService.getAdmin(token,true);
        ClassOffline classOffline = classOfflineRepository.findById(classId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));

        try {
            if (request.getStartDate() == null || request.getEndDate() == null) {
                log.error("StartDate or EndDate is null");
                throw new AppException(ErrorCode.INVALID_DATE_RANGE);
            }
            if (!request.getEndDate().after(request.getStartDate())) {
                log.error("EndDate is not after StartDate - startDate: {}, endDate: {}",
                        request.getStartDate(), request.getEndDate());
                throw new AppException(ErrorCode.INVALID_DATE_RANGE);
            }

            classOfflineMapper.updateClassOffline(request, classOffline);
            List<DayOfWeek> dayOfWeekList = request.getDayOfWeekList().stream()
                    .map(day -> DayOfWeek.valueOf(day.toUpperCase())) // Sử dụng java.time.DayOfWeek
                    .collect(Collectors.toList());
            classOffline.setDayOfWeekList(dayOfWeekList);

            ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
            LocalDate startDate = classOffline.getStartDate().toInstant().atZone(zoneId).toLocalDate();
            LocalDate endDate = classOffline.getEndDate().toInstant().atZone(zoneId).toLocalDate();

            List<ClassOfflineSession> newSessions = generateSessions(classOffline, startDate, endDate, dayOfWeekList);
            classOffline.getSessions().clear();
            classOffline.getSessions().addAll(newSessions);
            classOffline.setQuantitySession(classOffline.getSessions().size());
            classOffline.setUpdatedAt(new Date());

            classOfflineRepository.save(classOffline);

            return classOfflineMapper.classOfflineResponse(classOffline);

        } catch (IllegalArgumentException ex) {
            log.error("Invalid day of week: {}", ex.getMessage(), ex);
            throw new AppException(ErrorCode.UNEXPECTED_ERROR);
        } catch (AppException ex) {
            log.error("Error while updating class: {}", ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error occurred while updating class: {}", ex.getMessage(), ex);
            throw new AppException(ErrorCode.UNEXPECTED_ERROR);
        }
    }

    private List<ClassOfflineSession> generateSessions(ClassOffline classOffline, LocalDate startDate, LocalDate endDate, List<DayOfWeek> dayOfWeekList) {
        List<ClassOfflineSession> sessions = new ArrayList<>();
        LocalDate currentDate = startDate;
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");

        log.info("Generating sessions - startDate: {}, endDate: {}, dayOfWeekList: {}", startDate, endDate, dayOfWeekList);
        log.info("Running updated generateSessions version - 2025-03-20");

        while (!currentDate.isAfter(endDate)) {
            log.info("Checking date: {}, day of week: {}", currentDate, currentDate.getDayOfWeek());
            if (dayOfWeekList.contains(currentDate.getDayOfWeek())) {
                ClassOfflineSession session = new ClassOfflineSession();
                session.setClassOffline(classOffline);
                session.setSessionDate(Date.from(currentDate.atStartOfDay(zoneId).toInstant()));
                sessions.add(session);
                log.info("Added session for date: {}", currentDate);
            }
            currentDate = currentDate.plusDays(1);
        }
        log.info("Total sessions generated: {}", sessions.size());
        return sessions;
    }

    public ClassOfflineResponse getClass(Integer classId, String token)  {
        User user = tokenService.getUserFromToken(token);
        ClassOffline classOffline = classOfflineRepository.findById(classId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
        return classOfflineMapper.classOfflineResponse(classOffline);
    }

    public Page<ClassOfflineResponse> getAllClass(Integer courseId, Pageable pageable, String token)
            throws ParseException, JOSEException {
        User user = tokenService.getAdmin(token,true);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        Page<ClassOffline> classList = classOfflineRepository.findAllByCourse(course, pageable);
        return classList.map(classOfflineMapper::classOfflineResponse);
    }

    public ClassOfflineDeleteResponse deleteClassOffline(Integer classId, String token){
        User user = tokenService.getAdmin(token,true);
        ClassOffline classOffline = classOfflineRepository.findById(classId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
        classOffline.setStatus(false);
        classOfflineRepository.save(classOffline);

        ClassOfflineDeleteResponse result = new ClassOfflineDeleteResponse();
        result.setClassId(classId);
        result.setMessage("Delete success");
        result.setStatus(true);
        return ApiResponse.<ClassOfflineDeleteResponse>builder().result(result).build().getResult();
    }
}