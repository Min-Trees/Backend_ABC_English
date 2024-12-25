package com.ABCEnglish.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultTestDeleteResponse {
    int resultTestId;
    boolean status;
    String message;
}
