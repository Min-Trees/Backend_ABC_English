package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.LibraryItemRequest;
import com.ABCEnglish.dto.response.LibraryItemResponse;
import com.ABCEnglish.entity.LibraryItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")

public interface LibraryItemMapper {
    @Mapping(target = "library.libraryId", source = "libraryId")
    @Mapping(target = "libraryItemId", ignore = true) // tránh ghi đè khi tạo mới
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    LibraryItem toLibraryItemEntity(LibraryItemRequest request);
    LibraryItemResponse toResponse(LibraryItem entity);

    // List<Entity> → List<Response>
    List<LibraryItemResponse> toResponseList(List<LibraryItem> items);
}
