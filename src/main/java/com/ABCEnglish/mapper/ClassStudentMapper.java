package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.ClassStudentRequest;
import com.ABCEnglish.dto.response.ClassStudentResponse;
import com.ABCEnglish.entity.ClassStudent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClassStudentMapper {

    @Mapping(target = "user", ignore = true) // ✅ Không thể ánh xạ trực tiếp userId
    @Mapping(target = "classOffline", ignore = true) // ✅ Không thể ánh xạ trực tiếp classId
    ClassStudent toClassStudent(ClassStudentRequest request);

    @Mapping(source = "classStudentId", target = "classStudentId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.userId", target = "userId")// ✅ Sửa lại ánh xạ user
    @Mapping(source = "classOffline.name", target = "className") // ✅ Sửa lại ánh xạ classOffline
    ClassStudentResponse toClassStudentResponse(ClassStudent classStudent);

    void updateClassStudent(ClassStudentRequest request, @MappingTarget ClassStudent classStudent);
}