package com.ABCEnglish.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class SearchRequest {
    private String keyword;
    private String courseType; // khóa học, tài liệu, bài kiểm tra, bài học
    private Double minPrice;
    private Double maxPrice;
    private LocalDate startDate;
    private LocalDate endDate;
}
