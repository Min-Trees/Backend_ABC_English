package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.request.LessonRequest;
import com.ABCEnglish.dto.response.LessonDeleteResponse;
import com.ABCEnglish.dto.response.LessonResponse;
import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.Lesson;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exceptioin.AppException;
import com.ABCEnglish.exceptioin.ErrorCode;
import com.ABCEnglish.mapper.LessonMapper;
import com.ABCEnglish.reponsesitory.CourseRepository;
import com.ABCEnglish.reponsesitory.LessonRepository;
import com.ABCEnglish.reponsesitory.UserRepository;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LessonService {
    LessonRepository lessonRepository;
    LessonMapper lessonMapper;
    AuthenticationService authenticationService;
    UserRepository userRepository;
    CourseRepository courseRepository;
    public LessonResponse createdLesson(Integer courseId, LessonRequest request, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        // kiem tra su ton tai cua user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        // kiem tra su ton tai cua khoa hoc ( tai lieu thuoc khoa hoc )
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        Lesson lesson = lessonMapper.toLesson(request);
        lesson.setCreator(user);
        lesson.setCourse(course);

        lessonRepository.save(lesson);
        return lessonMapper.lessonResponse(lesson);
    }

    public LessonResponse updateLesson(Integer courseId, Integer lessonId,LessonRequest request, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        // kiem tra su ton tai cua user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        // kiem tra su ton tai cua khoa hoc ( tai lieu thuoc khoa hoc )
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));

        lessonMapper.updateLesson(request,lesson);
        Lesson updateLesson =lessonRepository.save(lesson);
        return lessonMapper.lessonResponse(lesson);
    }

    public Page<LessonResponse> getAllLesson(Pageable pageable){
        // thuc hien phan trang
        Page<Lesson> lessons = lessonRepository.findAll(pageable);
        return lessons.map(lessonMapper::lessonResponse);
    }

    public LessonDeleteResponse deleteLesson(Integer courseId, Integer lessonId, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        // kiem tra su ton tai cua user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        // kiem tra su ton tai cua khoa hoc ( tai lieu thuoc khoa hoc )
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));
        lessonRepository.delete(lesson);
        LessonDeleteResponse lessonDeleteResponse = new LessonDeleteResponse();
        lessonDeleteResponse.setLessonId(lessonId);
        lessonDeleteResponse.setStatus(false);
        lessonDeleteResponse.setMessage("delete success");
        return ApiResponse.<LessonDeleteResponse>builder().result(lessonDeleteResponse).build().getResult();
    }
}
