package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.ResultExerciseRequest;
import com.ABCEnglish.dto.response.ResultExerciseResponse;
import com.ABCEnglish.entity.ResultExercises;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResultExerciseMapper {
    ResultExercises toResult(ResultExerciseRequest request);
    @Mapping(source = "exercise.exerciseId", target = "exerciseId")
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "createdAt", target = "createdAt") // Mapping createdAt
    @Mapping(source = "updatedAt", target = "updatedAt")
    ResultExerciseResponse resultResponse(ResultExercises resultExercises);
}
