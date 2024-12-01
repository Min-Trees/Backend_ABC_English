package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.UserResponse;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.entity.UserLog;
import com.ABCEnglish.exceptioin.AppException;
import com.ABCEnglish.exceptioin.ErrorCode;
import com.ABCEnglish.reponsesitory.UserLogRepository;
import com.ABCEnglish.reponsesitory.UserRepository;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;

@Service
public class UserLogService {
    @Autowired
    private UserLogRepository userLogRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRepository userRepository;
    public void saveLog(String token, String ipAddress, String action, String method) throws ParseException, JOSEException {
        UserLog log = new UserLog();
        log.setIpAddress(ipAddress);
        log.setAction(action);
        log.setMethod(method);
        LocalDateTime currentDateTime = LocalDateTime.now();
        log.setTimestamp(currentDateTime);

        if (!"Not Provided".equals(token)) {
            IntrospectRequest introspectRequest = new IntrospectRequest();
            introspectRequest.setToken(token);
            Integer userId = authenticationService.introspectToken(introspectRequest).getUserId();

            // Kiểm tra user tồn tại
            userRepository.findById(userId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            log.setUserId(userId);
            userLogRepository.save(log);
        }

    }

}
