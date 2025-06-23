package com.ABCEnglish.dto.request;

import com.ABCEnglish.entity.TypeLibrary;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LibraryItemRequest {
    Integer itemId;
    String libraryItemDescription;
    TypeLibrary type;
    Integer libraryId;
}
