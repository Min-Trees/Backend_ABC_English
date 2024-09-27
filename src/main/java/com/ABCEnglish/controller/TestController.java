package com.ABCEnglish.controller;

import com.ABCEnglish.dto.response.TestResponse;
import com.ABCEnglish.service.TestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestController {
    @Autowired
    private TestService testService;
    @GetMapping()
    public ResponseEntity<Page<TestResponse>> getTest(Pageable pageable){
        Page<TestResponse> responses = testService.findAll(pageable);
        return ResponseEntity.ok(responses);
    }
}
