package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.ExerciseBlockRequest;
import com.ABCEnglish.dto.response.ExerciseBlockResponse;
import com.ABCEnglish.entity.ExerciseBlock;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")

public interface ExerciseBlockMapper {
    @Mapping(source = "exercise.exerciseId", target = "exerciseId")
    ExerciseBlockResponse toResponse(ExerciseBlock block);

    // Convert list entity → list response
    List<ExerciseBlockResponse> toResponseList(List<ExerciseBlock> blocks);

    // Convert request → entity (for create)
    @Mapping(target = "exercise.exerciseId", source = "exerciseId")
    @Mapping(target = "exercise", ignore = true) // để tự set thủ công exercise bằng service
    @Mapping(target = "exerciseBlockId", ignore = true)
    @Mapping(target = "orderIndex", source = "orderIndex") // không map id
    ExerciseBlock toEntity(ExerciseBlockRequest request);

    // Optional: update entity từ request (for update)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "exercise", ignore = true) // không cập nhật exercise
    void updateEntityFromRequest(ExerciseBlockRequest request, @MappingTarget ExerciseBlock block);
}
