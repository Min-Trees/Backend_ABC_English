package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.ExerciseRequest;
import com.ABCEnglish.dto.response.ExerciseResponse;
import com.ABCEnglish.entity.Exercises;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface ExerciseMapper {
    Exercises toExercise(ExerciseRequest request);

    @Mapping(source = "exerciseId", target = "exerciseId")
    @Mapping(source = "lesson.name", target = "lesson")
    @Mapping(source = "creator.userId", target = "creator")  // Mapping creator's userId to creator field
    @Mapping(source = "createdAt", target = "createdAt")      // Mapping createdAt field
    @Mapping(source = "updatedAt", target = "updatedAt")
    ExerciseResponse exerciseResponse(Exercises exercises);

    void updateExercises(ExerciseRequest request, @MappingTarget Exercises exercises);
}
