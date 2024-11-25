package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.ClassOfflineRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.ClassOfflineResponse;
import com.ABCEnglish.entity.ClassOffline;
import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.DayOfWeek;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exceptioin.AppException;
import com.ABCEnglish.exceptioin.ErrorCode;
import com.ABCEnglish.mapper.ClassOfflineMapper;
import com.ABCEnglish.reponsesitory.ClassOfflineRepository;
import com.ABCEnglish.reponsesitory.CourseRepository;
import com.ABCEnglish.reponsesitory.UserRepository;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

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

    public ClassOfflineResponse addClass(ClassOfflineRequest request, Integer courseId, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        try {
            if (request.getStartDate() == null || request.getEndDate() == null) {
                throw new AppException(ErrorCode.INVALID_DATE_RANGE);
            }

            if (!request.getEndDate().after(request.getStartDate())) {
                throw new AppException(ErrorCode.INVALID_DATE_RANGE);
            }

            // Ánh xạ thủ công dayOfWeekList
            List<DayOfWeek> dayOfWeekList = request.getDayOfWeekList().stream()
                    .map(DayOfWeek::valueOf)
                    .toList();


            // Tạo ClassOffline từ request
            ClassOffline classOffline = classOfflineMapper.toClassOffline(request);
            classOffline.setCourse(course);
            classOffline.setCreatedAt(new Date());
            classOffline.setUpdatedAt(new Date());
            classOffline.setDayOfWeekList(dayOfWeekList);

            // Tính toán số lượng buổi học
            LocalDate startDate = classOffline.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endDate = classOffline.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            long weeksBetween = ChronoUnit.WEEKS.between(startDate, endDate);
            weeksBetween = weeksBetween == 0 ? 1 : weeksBetween;

            int totalSession = (int) (weeksBetween * classOffline.getDayOfWeekList().size());
            classOffline.setQuantitySession(totalSession);

            classOfflineRepository.save(classOffline);

            return classOfflineMapper.classOfflineResponse(classOffline);

        } catch (AppException ex) {
            log.error("Error while adding class: {}", ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error occurred while adding class: {}", ex.getMessage(), ex);
            throw new AppException(ErrorCode.UNEXPECTED_ERROR);
        }
    }

}