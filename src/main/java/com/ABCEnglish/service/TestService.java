package com.ABCEnglish.service;

import com.ABCEnglish.dto.response.TestResponse;
import com.ABCEnglish.entity.Test;
import com.ABCEnglish.mapper.TestMapper;
import com.ABCEnglish.reponsesitory.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
public class TestService {
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private TestMapper testMapper;

    public Page<TestResponse> findAll(Pageable pageable) {
        Page<Test> tests = testRepository.findAll(pageable);
        return tests.map(testMapper::testResponse);
    }
}
