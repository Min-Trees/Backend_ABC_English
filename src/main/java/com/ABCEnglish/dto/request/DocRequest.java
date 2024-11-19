package com.ABCEnglish.dto.request;

import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.Doc;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocRequest {
     String name;
     String content;
     String description;
     String url;
     String images;
     Doc.DocType type;
     Boolean status;
     Boolean isFree;
}
