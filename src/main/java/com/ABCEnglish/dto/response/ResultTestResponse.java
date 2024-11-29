package com.ABCEnglish.dto.response;

import com.ABCEnglish.entity.Test;
import com.ABCEnglish.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultTestResponse {
    private Integer resultTestId;
    private Integer testId;
    private Integer userId;
    private Double score;
    private java.util.Date createdAt;
    private java.util.Date updatedAt;
}
