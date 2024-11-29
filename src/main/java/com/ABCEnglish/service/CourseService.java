package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.ApiResponse;
import com.ABCEnglish.dto.request.CourseRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.CourseDeleteResponse;
import com.ABCEnglish.dto.response.CourseResponse;
import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.CourseOfUser;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exceptioin.AppException;
import com.ABCEnglish.exceptioin.ErrorCode;
import com.ABCEnglish.mapper.CourseMapper;
import com.ABCEnglish.reponsesitory.CourseOfUserRepository;
import com.ABCEnglish.reponsesitory.CourseRepository;
import com.ABCEnglish.reponsesitory.UserRepository;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public CourseResponse createCourse(CourseRequest request, IntrospectRequest token) throws ParseException, JOSEException {
        // Lấy userId từ token
        Integer userId = authenticationService.introspectToken(token).getUserId();
        System.out.println(authenticationService.introspectToken(token).getUserId());

        // Kiểm tra người dùng tồn tại
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        System.out.println(userRepository.findById(userId));
        // Ánh xạ từ CourseRequest sang Course
        Course course = courseMapper.toCourse(request);
        course.setCreator(user);
        // Tìm người dùng từ ID và gán vào Course

        if (request.getTeacher() != null) {
            User teacher = userRepository.findById(request.getTeacher().getUserId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            System.out.println("Teacher found: " + teacher);
            course.setTeacher(teacher);
        }
        course.setCreatedAt(new Date());
        course.setUpdatedAt(new Date());
        // Lưu đối tượng Course vào cơ sở dữ liệu
        courseRepository.save(course);
        // Trả về đối tượng CourseResponse
        return courseMapper.courseResponse(course);
    }

    public CourseResponse updateCourse(Integer courseId,CourseRequest request, IntrospectRequest token) throws ParseException, JOSEException {
        // lay userId tu token duoc giai ma
        Integer userId = authenticationService.introspectToken(token).getUserId();
        // kiem tra xem user co ton tai hay khong
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        // kiem tra xem nguoi tao co phai nguoi sua khong
        Course course = courseRepository.findByCourseIdAndCreator(courseId, user)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        courseMapper.updatedCourse(request,course);
        // cap nhat nguoi tao
        course.setCreator(user);
        if (request.getTeacher() != null) {
            User teacher = userRepository.findById(request.getTeacher().getUserId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            // cap nhat giao vien
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
    public Page<CourseResponse> getAllCourseByTeacher(Pageable pageable, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Page<Course> courses = courseRepository.findAllCoureByCreator(user,pageable);
        return courses.map(courseMapper::courseResponse);
    }

    public CourseResponse getCourse(Integer courseId, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        return courseMapper.courseResponse(course);
    }

    public CourseDeleteResponse deleteCourse(Integer courseId, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Course course = courseRepository.findByCourseIdAndCreator(courseId, user)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        courseRepository.delete(course);
        CourseDeleteResponse courseDeleteResponse = new CourseDeleteResponse();
        courseDeleteResponse.setCourseId(courseId);
        courseDeleteResponse.setStatus(false);
        courseDeleteResponse.setMessage("delete success");
        return ApiResponse.<CourseDeleteResponse>builder().result(courseDeleteResponse).build().getResult();
    }
    public Page<CourseResponse> getCoursesByUser(IntrospectRequest token, Pageable pageable) throws ParseException, JOSEException {
        // Lấy userId từ token
        Integer userId = authenticationService.introspectToken(token).getUserId();

        // Kiểm tra người dùng có tồn tại hay không
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Lấy danh sách các bản ghi CourseOfUser
        Page<CourseOfUser> courseOfUsers = courseOfUserRepository.findAllByUser(user, pageable);

        // Chuyển đổi danh sách CourseOfUser sang CourseResponse
        return courseOfUsers.map(courseOfUser ->
                courseMapper.courseResponse(courseOfUser.getCourse())
        );
    }



}
