package com.ABCEnglish.dto.request;

import com.ABCEnglish.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SocialRequest {
     User user;
     String content;
     String title;
     Set<String> images;
     Date createdAt;
     Date updatedAt;
}
