package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.TestRequest;
import com.ABCEnglish.dto.response.TestResponse;
import com.ABCEnglish.entity.Test;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TestMapper {
    TestResponse testResponse(Test test);
    Test toTest(TestRequest testRequest);
    void updateTestResponse(TestRequest testResponse, @MappingTarget Test test);
}
