package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.request.ResultTestRequest;
import com.ABCEnglish.dto.response.ResultTestDeleteResponse;
import com.ABCEnglish.entity.ResultTest;
import com.ABCEnglish.entity.Test;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exceptioin.AppException;
import com.ABCEnglish.exceptioin.ErrorCode;
import com.ABCEnglish.reponsesitory.ResultTestReposotory;
import com.ABCEnglish.reponsesitory.TestRepository;
import com.ABCEnglish.reponsesitory.UserRepository;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Optional;

@Service
public class ResultTestService {
    @Autowired
    ResultTestReposotory resultTestReposotory;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TestRepository testRepository;
    @Autowired
    AuthenticationService authenticationService;
    public ResultTest create(ResultTestRequest testRequest, IntrospectRequest token)  throws ParseException, JOSEException {
        // Lấy userId từ token
        Integer userId = authenticationService.introspectToken(token).getUserId();
        System.out.println(authenticationService.introspectToken(token).getUserId());
        // Kiểm tra người dùng tồn tại
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        ResultTest resultTest = new ResultTest();
        Optional<Test> test = testRepository.findById(testRequest.getTestId());
        if (!test.isPresent()) {
            throw new AppException(ErrorCode.TEST_NOT_FOUND);
        }
        resultTest.setUser(user);
        resultTest.setTest(test.get());
        resultTest.setScore(testRequest.getScore());
        return resultTestReposotory.save(resultTest);
    }
    public ResultTestDeleteResponse delete(Integer testId,IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        // kiem tra su ton tai cua user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        resultTestReposotory.deleteById(testId);
        ResultTestDeleteResponse deleteResponse = new ResultTestDeleteResponse();
        deleteResponse.setResultTestId(testId);
        deleteResponse.setMessage("Successfully deleted test");
        deleteResponse.setStatus(false);
        return deleteResponse;
    }
}
