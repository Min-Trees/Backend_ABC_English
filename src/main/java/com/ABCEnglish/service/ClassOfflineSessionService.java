package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.entity.ClassOffline;
import com.ABCEnglish.entity.ClassOfflineSession;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exception.AppException;
import com.ABCEnglish.exception.ErrorCode;
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
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassOfflineSessionService {
    AuthenticationService authenticationService;
    UserRepository userRepository;
    ClassOfflineRepository classOfflineRepository;
    ClassOfflineSessionRepository classOfflineSessionRepository;

    public List<ClassOfflineSession> getClassOfflineSession(Integer classId, IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        ClassOffline classOfflineSession = classOfflineRepository.findById(classId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
        List<ClassOfflineSession> sessions = classOfflineSessionRepository.findByClassOffline(classOfflineSession);
        return sessions;
    }
}
