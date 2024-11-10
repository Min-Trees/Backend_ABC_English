package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.response.ResultTestResponse;
import com.ABCEnglish.entity.ResultTest;
import org.mapstruct.Mapper;

@Mapper
public interface ResultTestMapper {
    ResultTestResponse toResultTestResponse(ResultTest resultTest);
}
