package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.LibraryDocRequest;
import com.ABCEnglish.dto.response.LibraryDocResponse;
import com.ABCEnglish.dto.response.LibraryQuestionResponse;
import com.ABCEnglish.entity.LibraryDoc;
import com.ABCEnglish.entity.LibraryQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface LibraryDocMapper {

    @Mapping(target = "library.libraryId", source="libraryId")
    @Mapping(target = "doc.docId", source = "docId")
    @Mapping(target = "libraryItemId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    LibraryDoc toEntity(LibraryDocRequest request);

    @Mapping(target = "docId", source = "doc.docId")
    @Mapping(target = "docName", source = "doc.name")
    @Mapping(target = "docType", source = "doc.type")
    LibraryDocResponse toResponse(LibraryDoc entity);

    @Mapping(target = "library.libraryId", source = "request.libraryId")
    @Mapping(target = "doc.docId", source = "request.docId")
    @Mapping(target = "libraryItemDescription", source = "request.libraryItemDescription")
    @Mapping(target = "libraryItemId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    LibraryDoc updateFromRequest(LibraryDocRequest request,LibraryDoc entity);

}
