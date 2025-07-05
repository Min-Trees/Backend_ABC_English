package com.ABCEnglish.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LibraryQuestionDeleteResponse {
    int libraryId;
    boolean status;
    String message;
}
