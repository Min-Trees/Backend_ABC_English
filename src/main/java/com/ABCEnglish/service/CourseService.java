package com.ABCEnglish.service;

import com.ABCEnglish.configuration.JwtUtil;
import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.CourseRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.CourseDeleteResponse;
import com.ABCEnglish.dto.response.CourseResponse;
import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.CourseOfUser;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exception.AppException;
import com.ABCEnglish.exception.ErrorCode;
import com.ABCEnglish.mapper.CourseMapper;
import com.ABCEnglish.repository.CourseOfUserRepository;
import com.ABCEnglish.repository.CourseRepository;
import com.ABCEnglish.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final CourseOfUserRepository courseOfUserRepository;
    private final UserService userService;
    private final LessonService lessonService;
    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private TokenService tokenService;

    // sua xong create course
    public CourseResponse createCourse(CourseRequest request, String token) throws ParseException, JOSEException {
        // Lấy userId từ token

        // Kiểm tra người dùng tồn tại
        User user = tokenService.getAdmin(token,true);
        Course course;
        try {
            course = courseMapper.toCourse(request);
            course.setCreator(user);
        } catch (Exception e) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }

        if (request.getTeacher() != null) {
            User teacher = userRepository.findById(request.getTeacher().getUserId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            System.out.println("Teacher found: " + teacher);
            course.setTeacher(teacher);
        }
        course.setCreatedAt(new Date());
        course.setUpdatedAt(new Date());
        courseRepository.save(course);
        return courseMapper.courseResponse(course);
    }

    public CourseResponse updateCourse(Integer courseId,CourseRequest request, String token) throws ParseException, JOSEException {
       User user = tokenService.getAdmin(token,true);
        Course course = courseRepository.findByCourseIdAndCreator(courseId, user)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        courseMapper.updatedCourse(request,course);
        course.setCreator(user);
        if (request.getTeacher() != null) {
            User teacher = userRepository.findById(request.getTeacher().getUserId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            course.setTeacher(teacher);
        }
        course.setUpdatedAt(new Date());
        Course updateCourse = courseRepository.save(course);
        // tra ve theo dinh dang courseResponse
        return courseMapper.courseResponse(updateCourse);
    }

    public Page<CourseResponse> getAllCourse(Pageable pageable){
        // thuc hien phan trang
        Page<Course> courses = courseRepository.findAllBy(pageable);
        return courses.map(courseMapper::courseResponse);
    }

    public CourseResponse getCourse(Integer courseId, String token) throws ParseException, JOSEException {
        User user = tokenService.getUserFromToken(token);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        return courseMapper.courseResponse(course);
    }

    public CourseDeleteResponse deleteCourse(Integer courseId, String token) throws ParseException, JOSEException {
        User user = tokenService.getAdmin(token,true);
        Course course = courseRepository.findByCourseIdAndCreator(courseId, user)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        courseRepository.delete(course);
        CourseDeleteResponse courseDeleteResponse = new CourseDeleteResponse();
        courseDeleteResponse.setCourseId(courseId);
        courseDeleteResponse.setStatus(false);
        courseDeleteResponse.setMessage("delete success");
        lessonService.deleteAllLessonByCourse(course);
        return ApiResponse.<CourseDeleteResponse>builder().result(courseDeleteResponse).build().getResult();
    }
    public Page<CourseResponse> getCoursesByUser(String token, Pageable pageable) throws ParseException, JOSEException {
        // Lấy userId từ token
        User user = tokenService.getUserFromToken(token);

        // Lấy danh sách các bản ghi CourseOfUser
        Page<CourseOfUser> courseOfUsers = courseOfUserRepository.findAllByUser(user, pageable);

        // Chuyển đổi danh sách CourseOfUser sang CourseResponse
        return courseOfUsers.map(courseOfUser ->
                courseMapper.courseResponse(courseOfUser.getCourse())
        );
    }

}
