package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.response.TestResponse;
import com.ABCEnglish.entity.Test;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TestMapper {
    TestResponse testResponse(Test test);
}
