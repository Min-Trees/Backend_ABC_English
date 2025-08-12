package com.ABCEnglish.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class SearchRequest {
    private String keyword;
    /**
     * Dataset type to search in.
     * Allowed values: course, lesson, doc, exercise, all
     */
    private String type; // khóa học, tài liệu, bài kiểm tra, bài học
    private Double minPrice;
    private Double maxPrice;
    private LocalDate startDate;
    private LocalDate endDate;
}
