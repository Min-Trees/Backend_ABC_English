package com.ABCEnglish.dto.response;

import com.ABCEnglish.entity.GrammarError;
import com.ABCEnglish.entity.Question;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.languagetool.rules.RuleMatch;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnswerEssayResponse {
    private Integer answerId;
    private String content;
    private Integer questionId;
    private Integer userId;
    private List<GrammarError> grammarErrors;

}
