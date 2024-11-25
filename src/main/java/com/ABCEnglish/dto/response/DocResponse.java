package com.ABCEnglish.dto.response;

import com.ABCEnglish.entity.Course;
import com.ABCEnglish.entity.Doc;
import com.ABCEnglish.entity.TypeDocs;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocResponse {
      Integer docId;
      String lesson;
      Integer creator; // Creator userId
      String name;
      String content;
      String description;
      String content;
      String url;
      String images;
      String type;
      Boolean status;
      Boolean isFree;
      Date createdAt;
      Date updatedAt;
}
