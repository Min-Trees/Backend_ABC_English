package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.request.ResultTestRequest;
import com.ABCEnglish.dto.response.ResultTestDeleteResponse;
import com.ABCEnglish.dto.response.ResultTestResponse;
import com.ABCEnglish.entity.ResultTest;
import com.ABCEnglish.entity.Test;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exceptioin.AppException;
import com.ABCEnglish.exceptioin.ErrorCode;
import com.ABCEnglish.mapper.ResultTestMapper;
import com.ABCEnglish.reponsesitory.ResultTestReposotory;
import com.ABCEnglish.reponsesitory.TestRepository;
import com.ABCEnglish.reponsesitory.UserRepository;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
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
    public List<ResultTestResponse> getALLResultTests() {
        List<ResultTest> resultTests = resultTestReposotory.findAll();
        return resultTests.stream().map(this::convertResponse).toList();
    }
    public ResultTestResponse getResultTestById(Integer id,IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        // kiem tra su ton tai cua user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Optional<ResultTest> resultTest = resultTestReposotory.findById(id);
        if (resultTest.isPresent()) {
            return convertResponse(resultTest.get());
        }
        else {
            throw new AppException(ErrorCode.RESULT_NOT_FOUND);
        }
    }
    public ResultTestResponse convertResponse(ResultTest resultTest) {
        ResultTestResponse resultTestResponse = new ResultTestResponse();
        resultTestResponse.setResultTestId(resultTest.getResultTestId());
        resultTestResponse.setTestId(resultTest.getResultTestId());
        resultTestResponse.setTestId(resultTest.getTest().getTestId());
        resultTestResponse.setUserId(resultTest.getUser().getUserId());
        resultTestResponse.setScore(resultTest.getScore());
        resultTestResponse.setCreatedAt(resultTest.getCreatedAt());
        resultTestResponse.setUpdatedAt(resultTest.getUpdatedAt());
        return resultTestResponse;
    }
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
    public ResultTestDeleteResponse delete(Integer resultTestId,IntrospectRequest token) throws ParseException, JOSEException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        // kiem tra su ton tai cua user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        resultTestReposotory.deleteById(resultTestId);
        ResultTestDeleteResponse deleteResponse = new ResultTestDeleteResponse();
        deleteResponse.setResultTestId(resultTestId);
        deleteResponse.setMessage("Successfully deleted test");
        deleteResponse.setStatus(false);
        return deleteResponse;
    }
}
