package com.ABCEnglish.dto.response;

import com.ABCEnglish.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)

public class SocialResponse {
    Integer socialId;
    User user;
    String title;
    String content;
    Set<String> images;
    Date createdAt;
    Date updatedAt;
}
