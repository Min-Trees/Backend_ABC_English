package com.ABCEnglish.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LibraryQuestionResponse {
    private Integer libraryItemId;
    private String libraryItemDescription;

    // From Question
    private Integer questionId;
    private String questionText;
    private BigDecimal score;
    private String questionType;
    private String skillType;
}
