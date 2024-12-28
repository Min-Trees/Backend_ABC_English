package com.ABCEnglish.mapper;

import com.ABCEnglish.dto.request.ClassOfflineRequest;
import com.ABCEnglish.dto.response.ClassOfflineResponse;
import com.ABCEnglish.entity.ClassOffline;
import com.ABCEnglish.entity.DayOfWeek;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ClassOfflineMapper {

    @Mapping(target = "dayOfWeekList", source = "dayOfWeekList")
    @Mapping(source = "status", target = "status")
    ClassOffline toClassOffline(ClassOfflineRequest request);

    @Mapping(source = "classId", target = "classId")
    @Mapping(source = "course.name", target = "course")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "quantitySession", target = "quantitySession")
    @Mapping(source = "dayOfWeekList", target = "dayOfWeekList")
    ClassOfflineResponse classOfflineResponse(ClassOffline classOffline);

    void updateClassOffline(ClassOfflineRequest request, @MappingTarget ClassOffline classOffline);

    // Phương thức ánh xạ tùy chỉnh cho List<DayOfWeek> sang List<String>
    default List<String> mapDayOfWeekListToStringList(List<DayOfWeek> dayOfWeekList) {
        if (dayOfWeekList == null) {
            return new ArrayList<>(); // Trả về danh sách rỗng thay vì null
        }
        List<String> result = new ArrayList<>();
        for (DayOfWeek day : dayOfWeekList) {
            result.add(day.name());
        }
        return result;
    }

    // Phương thức ánh xạ tùy chỉnh cho List<String> sang List<DayOfWeek>
    default List<DayOfWeek> mapStringListToDayOfWeekList(List<String> dayOfWeekList) {
        if (dayOfWeekList == null || dayOfWeekList.isEmpty()) {
            return new ArrayList<>(); // Trả về danh sách rỗng nếu danh sách là null hoặc trống
        }
        List<DayOfWeek> result = new ArrayList<>();
        for (String day : dayOfWeekList) {
            try {
                result.add(DayOfWeek.valueOf(day)); // Chuyển đổi chuỗi thành DayOfWeek
            } catch (IllegalArgumentException e) {
                // Log lỗi hoặc xử lý chuỗi không hợp lệ
                System.err.println("Invalid day: " + day); // Thông báo lỗi nếu giá trị không hợp lệ
            }
        }
        return result;
    }
}
