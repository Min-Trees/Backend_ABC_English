package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.request.LessonRequest;
import com.ABCEnglish.dto.response.LessonDeleteResponse;
import com.ABCEnglish.dto.response.LessonResponse;
import com.ABCEnglish.dto.response.QuestionResponse;
import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.Lesson;
import com.ABCEnglish.entity.LessonTopic;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exception.AppException;
import com.ABCEnglish.exception.ErrorCode;
import com.ABCEnglish.mapper.LessonMapper;
import com.ABCEnglish.repository.CourseRepository;
import com.ABCEnglish.repository.LessonRepository;
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
import java.util.Date;
import java.util.List;

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
    DocService docService;
    ExerciseService exerciseService;
    private final TokenService tokenService;

    public LessonResponse createdLesson(Integer courseId, LessonRequest request, String token) throws ParseException, JOSEException {
        User user = tokenService.getAdmin(token,true);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        Lesson lesson = lessonMapper.toLesson(request);
        lesson.setCreator(user);
        lesson.setCourse(course);
        lesson.setCreatedAt(new Date());
        lesson.setUpdatedAt(new Date());
        lessonRepository.save(lesson);
        return lessonMapper.lessonResponse(lesson);
    }

    public LessonResponse updateLesson(Integer courseId, Integer lessonId,LessonRequest request, String token) throws ParseException, JOSEException {
        User user = tokenService.getTeacherOrAdmin(token, true);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));

        lessonMapper.updateLesson(request,lesson);
        lesson.setUpdatedAt(new Date());
        Lesson updateLesson =lessonRepository.save(lesson);
        return lessonMapper.lessonResponse(lesson);
    }

    public Page<LessonResponse> getAllLesson( Integer courseId,Pageable pageable){
        // thuc hien phan trang
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        Page<Lesson> lessons = lessonRepository.findByCourse(course,pageable);
        return lessons.map(lessonMapper::lessonResponse);
    }

    public LessonResponse getLesson(Integer courseId, Integer lessonId, String token) throws ParseException, JOSEException {
        User user = tokenService.getUserFromToken(token);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));
        return lessonMapper.lessonResponse(lesson);
    }
    public LessonDeleteResponse deleteLesson(Integer courseId, Integer lessonId, String token) throws ParseException, JOSEException {
        User user = tokenService.getTeacherOrAdmin(token, true);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));
        lesson.setStatus(false);
        lessonRepository.save(lesson);
        LessonDeleteResponse lessonDeleteResponse = new LessonDeleteResponse();
        lessonDeleteResponse.setLessonId(lessonId);
        lessonDeleteResponse.setStatus(false);
        lessonDeleteResponse.setMessage("delete success");
        docService.deleteDocsByLesson(lesson);
        exerciseService.deleteAllExerciseByLesson(lesson);

        return ApiResponse.<LessonDeleteResponse>builder().result(lessonDeleteResponse).build().getResult();
    }

    public void deleteAllLessonByCourse(Course course){
        List<Lesson> lessons = lessonRepository.findByCourse(course);
        for(Lesson lesson: lessons){
            lesson.setStatus(false);
        }
        lessonRepository.saveAll(lessons);
    }
    public Page<LessonResponse> getLessonByTopic(String topic, Pageable pageable, String token) throws ParseException, JOSEException {
        User user = tokenService.getTeacherOrAdmin(token, true);
        LessonTopic lessonTopic = LessonTopic.valueOf(topic.toUpperCase());
        Page<Lesson> lessons = lessonRepository.findByTopic(lessonTopic,pageable);
        return lessons.map(lessonMapper::lessonResponse);
    }
}
