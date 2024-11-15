package com.ABCEnglish.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class SearchResult {
    private String name;
    private Double price;
    private String type;
    private String description;
    private String creatorName;
    private Date createdDate; // Ngày tạo (nếu cần)
}
