package com.ABCEnglish.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LibraryQuestionRequest {
    String libraryItemDescription;
    Integer questionId;
    Integer libraryId;
}
