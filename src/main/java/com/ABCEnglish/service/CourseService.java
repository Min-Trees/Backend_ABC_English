package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.CourseRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.CourseDeleteResponse;
import com.ABCEnglish.dto.response.CourseResponse;
import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exceptioin.AppException;
import com.ABCEnglish.exceptioin.ErrorCode;
import com.ABCEnglish.mapper.CourseMapper;
import com.ABCEnglish.reponsesitory.CourseRepository;
import com.ABCEnglish.reponsesitory.UserRepository;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    public CourseResponse createCourse(CourseRequest request, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user =  userRepository.findById(userId)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        Course course = courseMapper.toCourse(request);
        courseRepository.save(course);
        return courseMapper.courseResponse(course);
    }

    public CourseResponse updateCourse(Integer courseId,CourseRequest request, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Course course = courseRepository.findUserCreate(courseId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        courseMapper.updatedCourse(request,course);
        Course updateCourse = courseRepository.save(course);

        return courseMapper.courseResponse(updateCourse);
    }

    public Page<CourseResponse> getAllCourse(Pageable pageable){
        Page<Course> courses = courseRepository.findByAll(pageable);
        return courses.map(courseMapper::courseResponse);
    }

    public CourseDeleteResponse deleteCourse(Integer courseId, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Course course = courseRepository.findUserCreate(courseId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        courseRepository.delete(course);
        CourseDeleteResponse courseDeleteResponse = new CourseDeleteResponse();
        courseDeleteResponse.setCourseId(courseId);
        courseDeleteResponse.setStatus(false);
        courseDeleteResponse.setMessage("delete success");

        return ApiResponse.<CourseDeleteResponse>builder().result(courseDeleteResponse).build().getResult();
    }

}
