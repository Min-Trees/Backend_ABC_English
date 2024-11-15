package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.CourseRequest;
import com.ABCEnglish.dto.request.DocRequest;
import com.ABCEnglish.dto.response.DocResponse;
import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.Doc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DocMapper  {
    Doc toDoc(DocRequest request);
    @Mapping(source = "docId", target = "docId") // Mapping docId
    @Mapping(source = "creator.userId", target = "creator")
    @Mapping(source = "lesson.name", target = "lesson")// Mapping creator's userId to creator
    @Mapping(source = "createdAt", target = "createdAt") // Mapping createdAt
    @Mapping(source = "updatedAt", target = "updatedAt") // Mapping updatedAt
    DocResponse docResponse(Doc doc);

    void updateDoc(DocRequest request, @MappingTarget Doc doc);
}
