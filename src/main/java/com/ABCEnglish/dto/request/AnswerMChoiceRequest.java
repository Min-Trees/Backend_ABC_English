package com.ABCEnglish.dto.request;

import com.ABCEnglish.entity.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnswerMChoiceRequest {
     String content;
     Boolean isCorrect;
     Boolean status;
}
