package com.ABCEnglish.dto.request;

import com.ABCEnglish.entity.Test;
import com.ABCEnglish.entity.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Getter
public class ResultTestRequest {
    private Integer testId;
    private Integer userId;
    private Double score;
}
