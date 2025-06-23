package com.ABCEnglish.dto.response;

import com.ABCEnglish.entity.Library;
import com.ABCEnglish.entity.TypeLibrary;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LibraryItemResponse {
    Integer libraryItemId;
    Integer itemId;
    String libraryItemDescription;
    TypeLibrary type;
    Date createAt;
    Date updateAt;
    @ManyToOne
    Library library;
}
