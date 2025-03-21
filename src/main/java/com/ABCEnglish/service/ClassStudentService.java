package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.ClassStudentRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.ClassStudentResponse;
import com.ABCEnglish.entity.ClassOffline;
import com.ABCEnglish.entity.ClassStudent;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exception.AppException;
import com.ABCEnglish.exception.ErrorCode;
import com.ABCEnglish.mapper.ClassStudentMapper;
import com.ABCEnglish.repository.ClassOfflineRepository;
import com.ABCEnglish.repository.ClassStudentRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassStudentService {

     AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final ClassOfflineRepository classOfflineRepository;
    private final ClassStudentMapper classStudentMapper;
    private final ClassStudentRepository classStudentRepository;

    public ClassStudentResponse addClass(ClassStudentRequest request, IntrospectRequest token) throws ParseException, JOSEException {
         Integer userId = authenticationService.introspectToken(token).getUserId();

         User user = userRepository.findById(userId)
                 .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        ClassOffline classOffline = classOfflineRepository.findById(request.getClassId())
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));

        User student = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        ClassStudent classStudent = classStudentMapper.toClassStudent(request);
        classStudent.setClassOffline(classOffline);
        classStudent.setUser(student);
        classStudentRepository.save(classStudent);

        return classStudentMapper.toClassStudentResponse(classStudent);
//        ClassStudent classStudent = new ClassStudent();
//        classStudent.setClassOffline(classOffline);
//        classStudent.setUser(user);
//        classStudentRepository.save(classStudent);
//
//        ClassStudentResponse classStudentResponse = new ClassStudentResponse();
//        classStudentResponse.setClassStudentId(classStudent.getClassStudentId());
//        classStudentResponse.setClassName(classOffline.getName());
//        classStudentResponse.setUsername(user.getFullName());
//
//        return classStudentResponse;

    }

    public ClassStudentResponse updateClass(ClassStudentRequest request, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user;
        user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        ClassOffline classOffline = classOfflineRepository.findById(request.getClassId())
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));

        User student = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        ClassStudent classStudent = classStudentMapper.toClassStudent(request);
        classStudent.setUser(student);
        classStudent.setClassOffline(classOffline);
        classStudentRepository.save(classStudent);
        return classStudentMapper.toClassStudentResponse(classStudent);
    }

    public Page<ClassStudentResponse> getAllClass(Pageable pageable, Integer classId) {

        ClassOffline classOffline = classOfflineRepository.findById(classId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));

        Page<ClassStudent> classStudents = classStudentRepository.findByClassOffline(pageable,classOffline);
        return classStudents.map(classStudentMapper::toClassStudentResponse);
    }


    public String deleteStudentOfClass(ClassStudentRequest request, IntrospectRequest token) throws ParseException, JOSEException {

        Integer userId = authenticationService.introspectToken(token).getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User student = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        ClassOffline classOffline = classOfflineRepository.findById(request.getClassId())
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
        ClassStudent classStudent = classStudentRepository.findByUserAndClassOffline(user,classOffline)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        classStudentRepository.delete(classStudent);
        return "Delete student success";
    }

    public List<ClassStudentResponse> getAllStudentsOfClass(Integer classId, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        ClassOffline classOffline = classOfflineRepository.findById(classId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
        List<ClassStudent> classStudents = classStudentRepository.findByClassOffline(classOffline);
        return classStudents.stream().map(classStudentMapper::toClassStudentResponse).collect(Collectors.toList());
    }

}
