package com.ABCEnglish.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassOfflineDeleteResponse {
    int classId;
    boolean status;
    String message;
}
