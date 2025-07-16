package com.ABCEnglish.dto.response;

import com.ABCEnglish.entity.Doc;
import com.ABCEnglish.entity.Library;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.Date;
@Data
public class LibraryDocResponse {
    private Integer libraryItemId;
    private String libraryItemDescription;
    private Integer docId;
    private String docName;
    private String docType;
    private Date createdAt;
    private Date updatedAt;
    private Library library;
}
