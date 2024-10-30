package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.request.TestRequest;
import com.ABCEnglish.dto.response.TestResponse;
import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.Test;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exceptioin.AppException;
import com.ABCEnglish.exceptioin.ErrorCode;
import com.ABCEnglish.mapper.TestMapper;
import com.ABCEnglish.reponsesitory.CourseRepository;
import com.ABCEnglish.reponsesitory.TestRepository;
import com.ABCEnglish.reponsesitory.UserRepository;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;


@Service
public class TestService {
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private TestMapper testMapper;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CourseRepository courseRepository;
    public Page<TestResponse> findAll(Pageable pageable) {
        Page<Test> tests = testRepository.findAll(pageable);
        return tests.map(testMapper::testResponse);
    }
    public TestResponse createTest(TestRequest testRequest, IntrospectRequest token)  throws ParseException, JOSEException {

        Integer userId = authenticationService.introspectToken(token).getUserId();
        System.out.println(authenticationService.introspectToken(token).getUserId());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        System.out.println(userRepository.findById(userId));
        Test test = testMapper.toTest(testRequest);
        if(test.getCourse()!=null){
            Course course = courseRepository.findByCourseId(test.getCourse().getCourseId())
                    .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

            test.setCourse(course);
        }
        testRepository.save(test);
        return testMapper.testResponse(test);
    }
    public TestResponse updateTest(Integer testId,TestRequest testRequest, IntrospectRequest token)  throws ParseException, JOSEException {

        Integer userId = authenticationService.introspectToken(token).getUserId();
        System.out.println(authenticationService.introspectToken(token).getUserId());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        System.out.println(userRepository.findById(userId));
        Test test = testRepository.findById(testId).orElseThrow(()-> new AppException(ErrorCode.TEST_NOT_FOUND));
        testMapper.updateTestResponse(testRequest,test);

        if(test.getCourse()!=null){
            Course course = courseRepository.findByCourseId(test.getCourse().getCourseId())
                    .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

            test.setCourse(course);
        }
        testRepository.save(test);
        return testMapper.testResponse(test);
    }
    public String deleteTest(Integer testId, IntrospectRequest token)  throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        System.out.println(authenticationService.introspectToken(token).getUserId());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        System.out.println(userRepository.findById(userId));
        Test test = testRepository.findById(testId).orElseThrow(()-> new AppException(ErrorCode.TEST_NOT_FOUND));
        testRepository.delete(test);
        return "Deleted";
    }
}
