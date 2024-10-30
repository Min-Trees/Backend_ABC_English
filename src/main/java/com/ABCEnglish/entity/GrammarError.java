package com.ABCEnglish.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class GrammarError {
    private String message;
    private List<String> suggestions;  // Thêm thuộc tính suggestions
    private int start;
    private int end;

        // Constructor mới chấp nhận message, suggestions, start, và end
        public GrammarError(String message, List<String> suggestions, int start, int end) {
            this.message = message;
            this.suggestions = suggestions;
            this.start = start;
            this.end = end;
        }
}
